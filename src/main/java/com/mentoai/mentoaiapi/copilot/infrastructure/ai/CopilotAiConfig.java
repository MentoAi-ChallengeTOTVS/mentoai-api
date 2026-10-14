package com.mentoai.mentoaiapi.copilot.infrastructure.ai;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.AiOrchestrator;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.config.AiProperties;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GeminiAiProvider;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.provider.GroqAiProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CopilotAiConfig {
    @Bean("copilotAiProvider")
    public AiProvider copilotAiProvider(GeminiAiProvider gemini, RestClient.Builder builder, AiProperties properties) {
        return new AiOrchestrator(gemini, new GroqAiProvider(builder, properties), properties);
    }
}
