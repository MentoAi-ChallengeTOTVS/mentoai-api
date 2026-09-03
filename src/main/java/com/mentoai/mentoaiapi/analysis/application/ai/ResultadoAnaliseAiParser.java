package com.mentoai.mentoaiapi.analysis.application.ai;

import com.mentoai.mentoaiapi.analysis.application.dto.EntradaAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.dto.SinalComercialGerado;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class ResultadoAnaliseAiParser {

    static final String SCHEMA_RESOURCE = "ai/analise-comercial-v1.schema.json";

    private final JsonMapper mapper;
    private final Schema schema;

    public ResultadoAnaliseAiParser() {
        mapper = JsonMapper.builder()
                .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .build();
        try {
            String schemaJson = new ClassPathResource(SCHEMA_RESOURCE).getContentAsString(StandardCharsets.UTF_8);
            schema = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12)
                    .getSchema(schemaJson, InputFormat.JSON);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Não foi possível carregar o JSON Schema da análise V1", exception);
        }
    }

    public ResultadoAnaliseAi parse(AiResponse resposta, EntradaAnaliseAi entrada) {
        Objects.requireNonNull(entrada, "A entrada da análise é obrigatória");
        if (resposta == null) {
            throw new IllegalStateException("A IA não retornou uma resposta para análise");
        }

        ResultadoAnaliseAi resultado;
        try {
            JsonNode json = mapper.readTree(resposta.content());
            if (json == null || !schema.validate(json).isEmpty()) {
                throw new IllegalStateException("A resposta da IA não atende ao contrato de análise V1");
            }
            resultado = mapper.treeToValue(json, ResultadoAnaliseAi.class);
        } catch (JacksonException | IllegalArgumentException exception) {
            // A causa pode conter dados da transcrição; não propagá-la ao tratamento global.
            throw new IllegalStateException("A resposta da IA não contém um JSON válido para o contrato de análise V1");
        }

        for (SinalComercialGerado sinal : resultado.sinaisComerciais()) {
            if (!entrada.transcricao().conteudo().contains(sinal.evidencia())) {
                throw new IllegalStateException("A resposta da IA contém evidência ausente da transcrição");
            }
        }
        if (resultado.sinaisComerciais().isEmpty() && !resultado.insights().isEmpty()) {
            throw new IllegalStateException("A resposta da IA contém insights sem sinais comerciais");
        }
        return resultado;
    }
}
