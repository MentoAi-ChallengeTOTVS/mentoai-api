package com.mentoai.mentoaiapi.analysis.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import java.util.Objects;

public record InsightGerado(TipoInsight tipo, String descricao, Severidade severidade) {

    public InsightGerado {
        Objects.requireNonNull(tipo, "O tipo do insight é obrigatório");
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do insight é obrigatória");
        }
        Objects.requireNonNull(severidade, "A severidade do insight é obrigatória");
    }
}
