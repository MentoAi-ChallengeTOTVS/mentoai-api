package com.mentoai.mentoaiapi.meeting.presentation.rest.response;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String segmento,
        String porte,
        LocalDateTime criacao,
        Boolean status
) {
}
