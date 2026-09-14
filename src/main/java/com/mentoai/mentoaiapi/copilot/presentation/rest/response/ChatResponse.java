package com.mentoai.mentoaiapi.copilot.presentation.rest.response;

import java.time.LocalDateTime;

public record ChatResponse(
        Long id,
        String titulo,
        Long usuarioId,
        LocalDateTime criacao
) {
}
