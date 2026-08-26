package com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

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
public class GeminiAiProvider {

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

    public AiResponse gerar(AiRequest request, String model) {
        validarConfiguracao(model);

        try {
            JsonNode response = restClient.post()
                    .uri("/v1beta/models/{model}:generateContent", model)
                    .header("x-goog-api-key", properties.getGemini().getApiKey())
                    .body(criarPayload(request))
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

    private Map<String, Object> criarPayload(AiRequest request) {
        return Map.of(
                "systemInstruction", Map.of("parts", List.of(Map.of("text", request.systemInstruction()))),
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", request.prompt()))
                )),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseSchema", request.responseSchema()
                )
        );
    }

    private String extrairConteudo(JsonNode response, String model) {
        JsonNode content = response == null
                ? null
                : response.at("/candidates/0/content/parts/0/text");
        String value = content == null || content.isMissingNode() ? null : content.stringValue();
        if (value == null || value.isBlank()) {
            throw AiProviderException.respostaInvalida(PROVIDER, model);
        }
        return value;
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
