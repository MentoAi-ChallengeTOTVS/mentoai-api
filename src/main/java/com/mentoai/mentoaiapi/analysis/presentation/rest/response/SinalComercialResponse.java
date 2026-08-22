package com.mentoai.mentoaiapi.analysis.presentation.rest.response;

import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import java.time.LocalDateTime;

public record SinalComercialResponse(
        Long id,
        Long analiseId,
        TipoSinalComercial tipo,
        String descricao,
        String evidencia,
        RelevanciaSinal relevancia,
        LocalDateTime criacao
) {
}
