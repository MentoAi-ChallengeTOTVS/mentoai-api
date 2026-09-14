package com.mentoai.mentoaiapi.analysis.presentation.rest.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.SinalComercialResponse;
import org.springframework.stereotype.Component;

@Component
public class SinalComercialRestMapper {

    public SinalComercialResponse toResponse(SinalComercial sinal) {
        return new SinalComercialResponse(sinal.getId(), sinal.getAnalise().getId(), sinal.getTipo(),
                sinal.getDescricao(), sinal.getEvidencia(), sinal.getRelevancia(), sinal.getCriacao());
    }
}
