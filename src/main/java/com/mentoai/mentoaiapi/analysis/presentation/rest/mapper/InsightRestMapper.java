package com.mentoai.mentoaiapi.analysis.presentation.rest.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.InsightResponse;
import org.springframework.stereotype.Component;

@Component
public class InsightRestMapper {

    public InsightResponse toResponse(Insight insight) {
        return new InsightResponse(insight.getId(), insight.getAnalise().getId(), insight.getTipo(),
                insight.getDescricao(), insight.getSeveridade(), insight.getCriacao());
    }
}
