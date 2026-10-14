package com.mentoai.mentoaiapi.copilot.application.repository;

import com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto.*;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import java.util.List;

public interface ContextoCopilotoRepository {
    List<ReuniaoContexto> reunioes(Long clienteId, int limite);
    List<InsightContexto> insights(Long clienteId, List<Long> reuniaoIds, List<TipoInsight> tipos, int limite);
    List<SinalContexto> sinais(Long clienteId, List<Long> reuniaoIds, List<TipoSinalComercial> tipos, int limite);
    List<AlertaContexto> alertas(List<Long> sinalIds, int limite);
}
