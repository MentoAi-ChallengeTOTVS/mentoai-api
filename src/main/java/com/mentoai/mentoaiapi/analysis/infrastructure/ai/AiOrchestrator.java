package com.mentoai.mentoaiapi.analysis.infrastructure.ai;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GeminiAiProvider;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GroqAiProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;

// @Component
public class AiOrchestrator implements AiProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiOrchestrator.class);

    private final GeminiAiProvider geminiAiProvider;
    private final GroqAiProvider groqAiProvider;
    private final AiProperties properties;

    public AiOrchestrator(
            GeminiAiProvider geminiAiProvider,
            GroqAiProvider groqAiProvider,
            AiProperties properties
    ) {
        this.geminiAiProvider = geminiAiProvider;
        this.groqAiProvider = groqAiProvider;
        this.properties = properties;
    }

    @Override
    public AiResponse gerar(AiRequest request) {
        String primaryModel = properties.getGemini().getPrimaryModel();
        try {
            return geminiAiProvider.gerar(request, primaryModel);
        } catch (AiProviderException exception) {
            validarFallback(exception);
            registrarFallback(exception, properties.getGemini().getFallbackModel());
        }

        String fallbackModel = properties.getGemini().getFallbackModel();
        try {
            return geminiAiProvider.gerar(request, fallbackModel);
        } catch (AiProviderException exception) {
            validarFallback(exception);
            registrarFallback(exception, properties.getGroq().getModel());
        }

        return groqAiProvider.gerar(request);
    }

    private void validarFallback(AiProviderException exception) {
        if (!exception.isFallbackElegivel()) {
            throw exception;
        }
    }

    private void registrarFallback(AiProviderException exception, String proximoModelo) {
        LOGGER.warn(
                "Fallback de IA acionado: provider={}, model={}, status={}, proximoModelo={}",
                exception.getProvider(),
                exception.getModel(),
                exception.getHttpStatus(),
                proximoModelo
        );
    }
}
