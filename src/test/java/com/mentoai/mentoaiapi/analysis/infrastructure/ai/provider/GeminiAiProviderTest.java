package com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.AiProviderException;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class GeminiAiProviderTest {

    private static final String BASE_URL = "https://mock-gemini.test";
    private static final String MODEL = "gemini-primary";

    private MockRestServiceServer server;
    private GeminiAiProvider provider;

    @BeforeEach
    void setUp() {
        AiProperties properties = new AiProperties();
        properties.getGemini().setApiKey("gemini-key");
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        provider = new GeminiAiProvider(builder.build(), properties);
    }

    @Test
    void deveGerarJsonEstruturado() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", "gemini-key"))
                .andExpect(content().string(containsString("\"systemInstruction\"")))
                .andExpect(content().string(containsString("\"responseMimeType\":\"application/json\"")))
                .andExpect(content().string(containsString("\"resumoExecutivo\"")))
                .andRespond(withSuccess(
                        "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"{\\\"resumoExecutivo\\\":\\\"ok\\\"}\"}]}}]}",
                        MediaType.APPLICATION_JSON
                ));

        AiResponse response = provider.gerar(request(), MODEL);

        assertEquals("{\"resumoExecutivo\":\"ok\"}", response.content());
        assertEquals("GEMINI", response.provider());
        assertEquals(MODEL, response.model());
        server.verify();
    }

    @ParameterizedTest
    @ValueSource(ints = {429, 500, 502, 503, 504})
    void deveClassificarFalhaHttpTemporariaComoElegivelParaFallback(int status) {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andRespond(withStatus(HttpStatus.valueOf(status)));

        AiProviderException exception = assertThrows(
                AiProviderException.class,
                () -> provider.gerar(request(), MODEL)
        );

        assertTrue(exception.isFallbackElegivel());
        assertEquals(status, exception.getHttpStatus());
        server.verify();
    }

    @Test
    void naoDeveFazerFallbackParaFalhaDeAutenticacao() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        AiProviderException exception = assertThrows(
                AiProviderException.class,
                () -> provider.gerar(request(), MODEL)
        );

        assertFalse(exception.isFallbackElegivel());
        assertEquals(401, exception.getHttpStatus());
        server.verify();
    }

    @Test
    void deveClassificarTimeoutComoElegivelParaFallback() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andRespond(withException(new SocketTimeoutException("timeout simulado")));

        AiProviderException exception = assertThrows(
                AiProviderException.class,
                () -> provider.gerar(request(), MODEL)
        );

        assertTrue(exception.isFallbackElegivel());
        server.verify();
    }

    @Test
    void deveClassificarFalhaDeConexaoComoElegivelParaFallback() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andRespond(withException(new ConnectException("conexão recusada simulada")));

        AiProviderException exception = assertThrows(
                AiProviderException.class,
                () -> provider.gerar(request(), MODEL)
        );

        assertTrue(exception.isFallbackElegivel());
        server.verify();
    }

    @Test
    void naoDeveFazerFallbackParaRespostaSemConteudo() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/" + MODEL + ":generateContent"))
                .andRespond(withSuccess("{\"candidates\":[]}", MediaType.APPLICATION_JSON));

        AiProviderException exception = assertThrows(
                AiProviderException.class,
                () -> provider.gerar(request(), MODEL)
        );

        assertFalse(exception.isFallbackElegivel());
        assertEquals(MODEL, exception.getModel());
        server.verify();
    }

    private AiRequest request() {
        return new AiRequest(
                "Retorne somente JSON.",
                "Analise a transcrição.",
                Map.of(
                        "type", "object",
                        "properties", Map.of("resumoExecutivo", Map.of("type", "string"))
                )
        );
    }
}
