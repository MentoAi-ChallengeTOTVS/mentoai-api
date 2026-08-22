package com.mentoai.mentoaiapi.alert.presentation.rest.response;

import java.time.LocalDateTime;

public record AlertaUsuarioResponse(
        Long id,
        Long alertaId,
        Long usuarioId,
        boolean lido,
        LocalDateTime lidoEm
) {
}
