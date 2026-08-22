package com.mentoai.mentoaiapi.analysis.presentation.rest.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.AnaliseIAResponse;
import org.springframework.stereotype.Component;

@Component
public class AnaliseIARestMapper {

    public AnaliseIAResponse toResponse(AnaliseIA analise) {
        return new AnaliseIAResponse(analise.getId(), analise.getReuniao().getId(), analise.getResumoExecutivo(),
                analise.getSentimentoGeral(), analise.getStatusProcessamento(), analise.getCriacao(),
                analise.getIniciadoEm(), analise.getFinalizadoEm(), analise.getMensagemErro());
    }
}
