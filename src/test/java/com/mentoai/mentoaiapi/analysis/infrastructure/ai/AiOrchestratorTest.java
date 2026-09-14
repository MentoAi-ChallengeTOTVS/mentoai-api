package com.mentoai.mentoaiapi.analysis.infrastructure.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GeminiAiProvider;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GroqAiProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AiOrchestratorTest {

    private static final String PRIMARY_MODEL = "gemini-primary";
    private static final String FALLBACK_MODEL = "gemini-fallback";
    private static final String GROQ_MODEL = "groq-model";

    @Mock
    private GeminiAiProvider geminiAiProvider;
    @Mock
    private GroqAiProvider groqAiProvider;

    private AiRequest request;
    private AiOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        AiProperties properties = new AiProperties();
        properties.getGemini().setPrimaryModel(PRIMARY_MODEL);
        properties.getGemini().setFallbackModel(FALLBACK_MODEL);
        properties.getGroq().setModel(GROQ_MODEL);
        request = new AiRequest("Sistema", "Prompt", Map.of("type", "object"));
        orchestrator = new AiOrchestrator(geminiAiProvider, groqAiProvider, properties);
    }

    @Test
    void deveRetornarRespostaDoGeminiPrimario() {
        AiResponse expected = new AiResponse("{}", "GEMINI", PRIMARY_MODEL);
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL)).thenReturn(expected);

        AiResponse response = orchestrator.gerar(request);

        assertSame(expected, response);
        verify(geminiAiProvider, never()).gerar(request, FALLBACK_MODEL);
        verifyNoInteractions(groqAiProvider);
    }

    @Test
    void deveUsarSegundoModeloGeminiAposFalhaElegivel() {
        AiResponse expected = new AiResponse("{}", "GEMINI", FALLBACK_MODEL);
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL))
                .thenThrow(temporaria("GEMINI", PRIMARY_MODEL));
        when(geminiAiProvider.gerar(request, FALLBACK_MODEL)).thenReturn(expected);

        AiResponse response = orchestrator.gerar(request);

        assertSame(expected, response);
        verifyNoInteractions(groqAiProvider);
    }

    @Test
    void deveUsarGroqAposFalhasElegiveisNosDoisModelosGemini() {
        AiResponse expected = new AiResponse("{}", "GROQ", GROQ_MODEL);
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL))
                .thenThrow(temporaria("GEMINI", PRIMARY_MODEL));
        when(geminiAiProvider.gerar(request, FALLBACK_MODEL))
                .thenThrow(temporaria("GEMINI", FALLBACK_MODEL));
        when(groqAiProvider.gerar(request)).thenReturn(expected);

        AiResponse response = orchestrator.gerar(request);

        assertSame(expected, response);
        verify(groqAiProvider).gerar(request);
    }

    @Test
    void naoDeveFazerFallbackParaFalhaNaoElegivelDoGeminiPrimario() {
        AiProviderException expected = AiProviderException.configuracao(
                "GEMINI",
                PRIMARY_MODEL,
                "chave inválida"
        );
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL)).thenThrow(expected);

        AiProviderException thrown = assertThrows(AiProviderException.class, () -> orchestrator.gerar(request));

        assertSame(expected, thrown);
        verify(geminiAiProvider, never()).gerar(request, FALLBACK_MODEL);
        verifyNoInteractions(groqAiProvider);
    }

    @Test
    void naoDeveFazerFallbackParaFalhaNaoElegivelDoSegundoGemini() {
        AiProviderException expected = AiProviderException.respostaInvalida("GEMINI", FALLBACK_MODEL);
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL))
                .thenThrow(temporaria("GEMINI", PRIMARY_MODEL));
        when(geminiAiProvider.gerar(request, FALLBACK_MODEL)).thenThrow(expected);

        AiProviderException thrown = assertThrows(AiProviderException.class, () -> orchestrator.gerar(request));

        assertSame(expected, thrown);
        verifyNoInteractions(groqAiProvider);
    }

    @Test
    void naoDeveMascararErroDeProgramacaoComoFallback() {
        IllegalStateException expected = new IllegalStateException("erro de programação");
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL)).thenThrow(expected);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> orchestrator.gerar(request));

        assertSame(expected, thrown);
        verify(geminiAiProvider, never()).gerar(request, FALLBACK_MODEL);
        verifyNoInteractions(groqAiProvider);
    }

    @Test
    void devePropagarFalhaFinalDoGroq() {
        AiProviderException expected = temporaria("GROQ", GROQ_MODEL);
        when(geminiAiProvider.gerar(request, PRIMARY_MODEL))
                .thenThrow(temporaria("GEMINI", PRIMARY_MODEL));
        when(geminiAiProvider.gerar(request, FALLBACK_MODEL))
                .thenThrow(temporaria("GEMINI", FALLBACK_MODEL));
        when(groqAiProvider.gerar(request)).thenThrow(expected);

        AiProviderException thrown = assertThrows(AiProviderException.class, () -> orchestrator.gerar(request));

        assertSame(expected, thrown);
        assertEquals(GROQ_MODEL, thrown.getModel());
    }

    private AiProviderException temporaria(String provider, String model) {
        return AiProviderException.indisponibilidadeTemporaria(provider, model, 503, null);
    }
}
