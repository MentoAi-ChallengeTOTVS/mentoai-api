package com.mentoai.mentoaiapi.analysis.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import java.time.LocalDateTime;

public record AnaliseFilaItemResponse(
        Long analiseId,
        Long reuniaoId,
        Long clienteId,
        String clienteNome,
        StatusProcessamento status,
        LocalDateTime criadoEm,
        LocalDateTime iniciadoEm,
        LocalDateTime finalizadoEm,
        String mensagemErro
) {
}
