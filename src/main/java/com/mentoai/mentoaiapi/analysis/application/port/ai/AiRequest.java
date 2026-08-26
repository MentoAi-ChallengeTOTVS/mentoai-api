package com.mentoai.mentoaiapi.analysis.application.port.ai;

import java.util.Map;
import java.util.Objects;

public record AiRequest(
        String systemInstruction,
        String prompt,
        Map<String, Object> responseSchema
) {

    public AiRequest {
        if (systemInstruction == null || systemInstruction.isBlank()) {
            throw new IllegalArgumentException("A instrução de sistema da IA é obrigatória");
        }
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("O prompt da IA é obrigatório");
        }
        responseSchema = Map.copyOf(Objects.requireNonNull(responseSchema, "O schema da resposta é obrigatório"));
    }
}
