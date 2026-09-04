package com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider;

import java.net.http.HttpClient;
import java.util.Map;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.AiProviderException;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

@Component
public class GeminiAiProvider implements AiProvider {

    static final String PROVIDER = "GEMINI";

    private final RestClient restClient;
    private final AiProperties properties;

    @Autowired
    public GeminiAiProvider(RestClient.Builder restClientBuilder, AiProperties properties) {
        this(criarRestClient(restClientBuilder, properties), properties);
    }

    GeminiAiProvider(RestClient restClient, AiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    @Override
    public AiResponse gerar(AiRequest request) {
        return gerar(request, properties.getGemini().getPrimaryModel());
    }

    public AiResponse gerar(AiRequest request, String model) {
        validarConfiguracao(model);

        try {
            JsonNode response = restClient.post()
                    .uri("/v1beta/interactions")
                    .header("x-goog-api-key", properties.getGemini().getApiKey())
                    .body(criarPayload(request, model))
                    .retrieve()
                    .body(JsonNode.class);

            String content = extrairConteudo(response, model);
            return new AiResponse(content, PROVIDER, model);
        } catch (AiProviderException exception) {
            throw exception;
        } catch (RestClientResponseException exception) {
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
                .baseUrl(properties.getGemini().getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    private Map<String, Object> criarPayload(AiRequest request, String model) {
        return Map.of(
                "model", model,
                "input", request.systemInstruction() + "\n\nDados de entrada (JSON):\n" + request.prompt(),
                "response_format", Map.of(
                        "type", "text",
                        "mime_type", "application/json",
                        "schema", request.responseSchema()
                )
        );
    }

    private String extrairConteudo(JsonNode response, String model) {
        if (response == null) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }
        JsonNode status = response.path("status");
        if (!status.isString() || !"completed".equals(status.stringValue())) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }

        JsonNode steps = response.path("steps");
        if (!steps.isArray()) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }

        boolean encontrouModelOutput = false;
        StringBuilder conteudo = new StringBuilder();
        for (JsonNode step : steps) {
            JsonNode stepType = step.path("type");
            if (!stepType.isString() || !"model_output".equals(stepType.stringValue())) {
                continue;
            }
            encontrouModelOutput = true;
            JsonNode content = step.path("content");
            if (!content.isArray()) {
                throw AiProviderException.respostaInvalida(PROVIDER, model);
            }
            for (JsonNode item : content) {
                JsonNode contentType = item.path("type");
                if (contentType.isString() && "text".equals(contentType.stringValue())) {
                    JsonNode text = item.path("text");
                    if (!text.isString()) {
                        throw AiProviderException.respostaInvalida(PROVIDER, model);
                    }
                    conteudo.append(text.stringValue());
                }
            }
        }
        if (!encontrouModelOutput || conteudo.toString().isBlank()) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }
        return conteudo.toString();
    }

    private void validarConfiguracao(String model) {
        if (properties.getGemini().getApiKey() == null || properties.getGemini().getApiKey().isBlank()) {
            throw AiProviderException.configuracao(PROVIDER, model, "GEMINI_API_KEY não configurada");
        }
        if (model == null || model.isBlank()) {
            throw AiProviderException.configuracao(PROVIDER, model, "modelo não configurado");
        }
    }
}
