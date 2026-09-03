package com.mentoai.mentoaiapi.analysis.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import java.util.Objects;

public record SinalComercialGerado(
        TipoSinalComercial tipo,
        String descricao,
        String evidencia,
        RelevanciaSinal relevancia
) {

    public SinalComercialGerado {
        Objects.requireNonNull(tipo, "O tipo do sinal é obrigatório");
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do sinal é obrigatória");
        }
        if (evidencia == null || evidencia.isBlank()) {
            throw new IllegalArgumentException("A evidência do sinal é obrigatória");
        }
        Objects.requireNonNull(relevancia, "A relevância do sinal é obrigatória");
    }
}
