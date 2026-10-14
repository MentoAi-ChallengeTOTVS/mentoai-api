package com.mentoai.mentoaiapi.copilot.domain.entity;
import com.mentoai.mentoaiapi.copilot.domain.enums.TipoFonteCopiloto;

public record FonteCopiloto(TipoFonteCopiloto tipo, Long id, Long clienteId, Long reuniaoId) {
}
