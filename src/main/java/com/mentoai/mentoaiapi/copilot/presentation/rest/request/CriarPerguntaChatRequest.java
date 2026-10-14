package com.mentoai.mentoaiapi.copilot.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPerguntaChatRequest(
        @NotBlank @Size(max = 4000) String pergunta
) {
}
