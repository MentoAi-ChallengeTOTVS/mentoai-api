package com.mentoai.mentoaiapi.analysis.presentation.rest.response;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import java.time.LocalDateTime;

public record AnaliseIAResponse(
        Long id,
        Long reuniaoId,
        String resumoExecutivo,
        SentimentoGeral sentimentoGeral,
        StatusProcessamento statusProcessamento,
        LocalDateTime criacao,
        LocalDateTime iniciadoEm,
        LocalDateTime finalizadoEm,
        String mensagemErro
) {
}
