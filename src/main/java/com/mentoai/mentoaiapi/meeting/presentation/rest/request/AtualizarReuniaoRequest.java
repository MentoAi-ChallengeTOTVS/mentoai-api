package com.mentoai.mentoaiapi.meeting.presentation.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record AtualizarReuniaoRequest(
        @NotNull LocalDateTime dataReuniao,
        @NotNull @PositiveOrZero Integer duracaoMinutos,
        @NotNull Long clienteId,
        @NotNull Long usuarioId
) {
}
