package com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.AiProviderException;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

@Component
public class GroqAiProvider {

    static final String PROVIDER = "GROQ";
    private static final int GROQ_CAPACITY_EXCEEDED = 498;

    private final RestClient restClient;
    private final AiProperties properties;

    @Autowired
    public GroqAiProvider(RestClient.Builder restClientBuilder, AiProperties properties) {
        this(criarRestClient(restClientBuilder, properties), properties);
    }

    GroqAiProvider(RestClient restClient, AiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public AiResponse gerar(AiRequest request) {
        String model = properties.getGroq().getModel();
        validarConfiguracao(model);

        try {
            JsonNode response = restClient.post()
                    .uri("/openai/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getGroq().getApiKey())
                    .body(criarPayload(request, model))
                    .retrieve()
                    .body(JsonNode.class);

            String content = extrairConteudo(response, model);
            return new AiResponse(content, PROVIDER, model);
        } catch (AiProviderException exception) {
            throw exception;
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == GROQ_CAPACITY_EXCEEDED) {
                throw AiProviderException.indisponibilidadeTemporaria(
                        PROVIDER,
                        model,
                        GROQ_CAPACITY_EXCEEDED,
                        exception
                );
            }
            throw AiProviderException.http(PROVIDER, model, exception.getStatusCode(), exception);
        } catch (ResourceAccessException exception) {
            throw AiProviderException.indisponibilidadeTemporaria(PROVIDER, model, null, exception);
        } catch (HttpMessageConversionException exception) {
            throw AiProviderException.integracao(PROVIDER, model, exception);
        } catch (RestClientException exception) {
            throw AiProviderException.integracao(PROVIDER, model, exception);
        }
    }

    private static RestClient criarRestClient(RestClient.Builder builder, AiProperties properties) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(properties.getConnectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(properties.getReadTimeout());

        return builder.clone()
                .baseUrl(properties.getGroq().getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    private Map<String, Object> criarPayload(AiRequest request, String model) {
        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", request.systemInstruction()),
                        Map.of("role", "user", "content", request.prompt())
                ),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "analise_comercial",
                                "strict", false,
                                "schema", request.responseSchema()
                        )
                )
        );
    }

    private String extrairConteudo(JsonNode response, String model) {
        JsonNode content = response == null
                ? null
                : response.at("/choices/0/message/content");
        String value = content == null || content.isMissingNode() ? null : content.stringValue();
        if (value == null || value.isBlank()) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }
        return value;
    }

    private void validarConfiguracao(String model) {
        if (properties.getGroq().getApiKey() == null || properties.getGroq().getApiKey().isBlank()) {
            throw AiProviderException.configuracao(PROVIDER, model, "GROQ_API_KEY não configurada");
        }
        if (model == null || model.isBlank()) {
            throw AiProviderException.configuracao(PROVIDER, model, "GROQ_MODEL não configurado");
        }
    }
}
