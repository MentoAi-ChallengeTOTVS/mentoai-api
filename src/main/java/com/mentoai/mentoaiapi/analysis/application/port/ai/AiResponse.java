package com.mentoai.mentoaiapi.analysis.application.port.ai;

public record AiResponse(
        String content,
        String provider,
        String model
) {

    public AiResponse {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("O conteúdo retornado pela IA é obrigatório");
        }
        if (provider == null || provider.isBlank()) {
            throw new IllegalArgumentException("O provider da IA é obrigatório");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("O modelo da IA é obrigatório");
        }
    }
}
