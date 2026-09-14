package com.mentoai.mentoaiapi.copilot.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarChatRequest(
        @NotBlank String titulo,
        @NotNull Long usuarioId
) {
}
