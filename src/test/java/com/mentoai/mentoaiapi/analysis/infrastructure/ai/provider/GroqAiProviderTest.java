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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class GroqAiProviderTest {

    private static final String BASE_URL = "https://mock-groq.test";
    private static final String MODEL = "groq-model";

    private MockRestServiceServer server;
    private GroqAiProvider provider;

    @BeforeEach
    void setUp() {
        AiProperties properties = new AiProperties();
        properties.getGroq().setApiKey("groq-key");
        properties.getGroq().setModel(MODEL);
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        provider = new GroqAiProvider(builder.build(), properties);
    }

    @Test
    void deveGerarJsonEstruturado() {
        server.expect(requestTo(BASE_URL + "/openai/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer groq-key"))
                .andExpect(content().string(containsString("\"response_format\"")))
                .andExpect(content().string(containsString("\"json_schema\"")))
                .andExpect(content().string(containsString("\"resumoExecutivo\"")))
                .andRespond(withSuccess(
                        "{\"choices\":[{\"message\":{\"content\":\"{\\\"resumoExecutivo\\\":\\\"ok\\\"}\"}}]}",
                        MediaType.APPLICATION_JSON
                ));

        AiResponse response = provider.gerar(request());

        assertEquals("{\"resumoExecutivo\":\"ok\"}", response.content());
        assertEquals("GROQ", response.provider());
        assertEquals(MODEL, response.model());
        server.verify();
    }

    @ParameterizedTest
    @ValueSource(ints = {429, 498, 500, 502, 503, 504})
    void deveClassificarFalhaHttpTemporariaComoElegivelParaFallback(int status) {
        server.expect(requestTo(BASE_URL + "/openai/v1/chat/completions"))
                .andRespond(withStatus(HttpStatusCode.valueOf(status)));

        AiProviderException exception = assertThrows(AiProviderException.class, () -> provider.gerar(request()));

        assertTrue(exception.isFallbackElegivel());
        assertEquals(status, exception.getHttpStatus());
        server.verify();
    }

    @Test
    void naoDeveClassificarRequestInvalidoComoElegivelParaFallback() {
        server.expect(requestTo(BASE_URL + "/openai/v1/chat/completions"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        AiProviderException exception = assertThrows(AiProviderException.class, () -> provider.gerar(request()));

        assertFalse(exception.isFallbackElegivel());
        assertEquals(400, exception.getHttpStatus());
        server.verify();
    }

    @Test
    void deveClassificarTimeoutComoElegivelParaFallback() {
        server.expect(requestTo(BASE_URL + "/openai/v1/chat/completions"))
                .andRespond(withException(new SocketTimeoutException("timeout simulado")));

        AiProviderException exception = assertThrows(AiProviderException.class, () -> provider.gerar(request()));

        assertTrue(exception.isFallbackElegivel());
        server.verify();
    }

    @Test
    void deveClassificarFalhaDeConexaoComoElegivelParaFallback() {
        server.expect(requestTo(BASE_URL + "/openai/v1/chat/completions"))
                .andRespond(withException(new ConnectException("conexão recusada simulada")));

        AiProviderException exception = assertThrows(AiProviderException.class, () -> provider.gerar(request()));

        assertTrue(exception.isFallbackElegivel());
        server.verify();
    }

    @Test
    void naoDeveFazerFallbackQuandoOModeloNaoEstaConfigurado() {
        AiProperties properties = new AiProperties();
        properties.getGroq().setApiKey("groq-key");
        GroqAiProvider semModelo = new GroqAiProvider(RestClient.create(BASE_URL), properties);

        AiProviderException exception = assertThrows(AiProviderException.class, () -> semModelo.gerar(request()));

        assertFalse(exception.isFallbackElegivel());
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
