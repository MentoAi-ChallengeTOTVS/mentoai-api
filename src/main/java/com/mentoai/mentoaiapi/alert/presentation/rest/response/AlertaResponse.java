package com.mentoai.mentoaiapi.alert.presentation.rest.response;

import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import java.time.LocalDateTime;

public record AlertaResponse(
        Long id,
        Long sinalComercialId,
        PrioridadeAlerta prioridade,
        String motivo,
        LocalDateTime criacao
) {
}
