package com.mentoai.mentoaiapi.analysis.presentation.rest.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.AnaliseIAResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnaliseIARestMapper {

    private final InsightRestMapper insightMapper;
    private final SinalComercialRestMapper sinalMapper;

    public AnaliseIARestMapper(InsightRestMapper insightMapper, SinalComercialRestMapper sinalMapper) {
        this.insightMapper = insightMapper;
        this.sinalMapper = sinalMapper;
    }

    public AnaliseIAResponse toResponse(AnaliseIA analise, List<Insight> insights, List<SinalComercial> sinais) {
        return new AnaliseIAResponse(analise.getId(), analise.getReuniao().getId(), analise.getResumoExecutivo(),
                analise.getSentimentoGeral(), analise.getStatusProcessamento(), analise.getCriacao(),
                analise.getIniciadoEm(), analise.getFinalizadoEm(), analise.getMensagemErro(),
                insights.stream().map(insightMapper::toResponse).toList(),
                sinais.stream().map(sinalMapper::toResponse).toList());
    }
}
