package com.mentoai.mentoaiapi.analysis.presentation.rest.response;

import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import java.time.LocalDateTime;

public record InsightResponse(
        Long id,
        Long analiseId,
        TipoInsight tipo,
        String descricao,
        Severidade severidade,
        LocalDateTime criacao
) {
}
