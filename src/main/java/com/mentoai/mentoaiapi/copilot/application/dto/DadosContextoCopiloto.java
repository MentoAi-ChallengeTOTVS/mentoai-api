package com.mentoai.mentoaiapi.copilot.application.dto;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import java.util.List;

public record DadosContextoCopiloto(
        Cliente cliente,
        List<ContextoCopiloto.ReuniaoContexto> reunioes,
        List<ContextoCopiloto.InsightContexto> insights,
        List<ContextoCopiloto.SinalContexto> sinais,
        List<ContextoCopiloto.AlertaContexto> alertas
) {}
