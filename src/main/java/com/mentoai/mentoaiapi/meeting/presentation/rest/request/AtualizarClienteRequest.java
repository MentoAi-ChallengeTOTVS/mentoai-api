package com.mentoai.mentoaiapi.meeting.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarClienteRequest(

        @NotBlank
        @Size(max = 255)
        String nome,

        @NotBlank
        @Size(max = 100)
        String segmento,

        @NotBlank
        @Size(max = 50)
        String porte

) {
}