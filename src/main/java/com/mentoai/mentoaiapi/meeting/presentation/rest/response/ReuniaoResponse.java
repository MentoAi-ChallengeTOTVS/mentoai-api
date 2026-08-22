package com.mentoai.mentoaiapi.meeting.presentation.rest.response;

import java.time.LocalDateTime;

public record ReuniaoResponse(
        Long id,
        LocalDateTime dataReuniao,
        Integer duracaoMinutos,
        Long clienteId,
        Long usuarioId,
        LocalDateTime criacao
) {
}
