package com.mentoai.mentoaiapi.copilot.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.*;
import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.copilot.domain.entity.FonteCopiloto;
import java.time.LocalDateTime;
import java.util.List;

public record ContextoCopiloto(
        ClienteContexto cliente,
        List<ReuniaoContexto> reunioes,
        List<InsightContexto> insights,
        List<SinalContexto> sinais,
        List<AlertaContexto> alertas,
        List<FonteCopiloto> fontes
) {
    public record ClienteContexto(Long id, String nome, String segmento, String porte, String resumoContextual) {}
    public record ReuniaoContexto(Long id, LocalDateTime data, Long analiseId, String resumo,
                                  SentimentoGeral sentimento) {}
    public record InsightContexto(Long id, Long reuniaoId, TipoInsight tipo, String descricao,
                                 Severidade severidade) {}
    public record SinalContexto(Long id, Long reuniaoId, TipoSinalComercial tipo, String descricao,
                               String evidencia, RelevanciaSinal relevancia) {}
    public record AlertaContexto(Long id, Long reuniaoId, Long sinalId, PrioridadeAlerta prioridade, String motivo) {}
}
