package com.mentoai.mentoaiapi.analysis.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import java.util.List;
import java.util.Objects;

public record ResultadoAnaliseAi(
        String resumoExecutivo,
        SentimentoGeral sentimentoGeral,
        List<SinalComercialGerado> sinaisComerciais,
        List<InsightGerado> insights
) {

    public ResultadoAnaliseAi {
        if (resumoExecutivo == null || resumoExecutivo.isBlank()) {
            throw new IllegalArgumentException("O resumo executivo é obrigatório");
        }
        Objects.requireNonNull(sentimentoGeral, "O sentimento geral é obrigatório");
        sinaisComerciais = List.copyOf(Objects.requireNonNull(sinaisComerciais, "Os sinais comerciais são obrigatórios"));
        insights = List.copyOf(Objects.requireNonNull(insights, "Os insights são obrigatórios"));
    }
}
