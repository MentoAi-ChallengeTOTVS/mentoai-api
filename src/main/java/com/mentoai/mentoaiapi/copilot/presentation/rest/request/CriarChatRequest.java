package com.mentoai.mentoaiapi.copilot.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CriarChatRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotNull @Positive Long clienteId
) {
}
