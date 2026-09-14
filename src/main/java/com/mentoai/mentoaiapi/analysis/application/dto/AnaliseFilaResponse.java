package com.mentoai.mentoaiapi.analysis.application.dto;

import java.util.List;
import java.util.Objects;

public record AnaliseFilaResponse(
        List<AnaliseFilaItemResponse> fila,
        List<AnaliseFilaItemResponse> finalizados
) {
    public AnaliseFilaResponse {
        fila = List.copyOf(Objects.requireNonNull(fila, "fila não pode ser nula"));
        finalizados = List.copyOf(Objects.requireNonNull(finalizados, "finalizados não pode ser nulo"));
    }
}
