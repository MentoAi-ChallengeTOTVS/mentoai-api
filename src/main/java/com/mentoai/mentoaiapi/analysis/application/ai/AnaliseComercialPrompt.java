package com.mentoai.mentoaiapi.analysis.application.ai;

import com.mentoai.mentoaiapi.analysis.application.dto.EntradaAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AnaliseComercialPrompt {

    private final JsonMapper mapper = JsonMapper.builder().build();
    private final String instrucoes;
    private final String schemaJson;

    public AnaliseComercialPrompt() {
        try {
            instrucoes = new ClassPathResource("ai/analise-comercial-v1.prompt.txt")
                    .getContentAsString(StandardCharsets.UTF_8);
            schemaJson = new ClassPathResource(ResultadoAnaliseAiParser.SCHEMA_RESOURCE)
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Não foi possível carregar o prompt e o schema da análise V1", exception);
        }
    }

    public AiRequest criarRequisicao(EntradaAnaliseAi entrada) {
        Objects.requireNonNull(entrada, "A entrada da análise é obrigatória");
        // Cada requisição recebe seu próprio objeto de schema, sem compartilhar mapas mutáveis.
        Map<String, Object> schema = mapper.readValue(schemaJson, new TypeReference<>() { });
        return new AiRequest(instrucoes, mapper.writeValueAsString(entrada), schema);
    }
}
