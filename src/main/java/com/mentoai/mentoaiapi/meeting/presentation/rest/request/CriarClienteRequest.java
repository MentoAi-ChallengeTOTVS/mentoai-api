package com.mentoai.mentoaiapi.meeting.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CriarClienteRequest(
        @NotBlank String nome,
        @NotBlank String segmento,
        @NotBlank String porte
) {
}
