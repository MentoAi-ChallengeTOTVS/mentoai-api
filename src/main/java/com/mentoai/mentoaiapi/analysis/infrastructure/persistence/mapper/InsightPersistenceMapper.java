package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.InsightJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class InsightPersistenceMapper {

    private final AnaliseIAPersistenceMapper analiseMapper;

    public InsightPersistenceMapper(AnaliseIAPersistenceMapper analiseMapper) {
        this.analiseMapper = analiseMapper;
    }

    public Insight toDomain(InsightJpaEntity entity) {
        return new Insight(entity.getId(), analiseMapper.toDomain(entity.getAnalise()), entity.getTipo(),
                entity.getDescricao(), entity.getSeveridade(), entity.getCriacao());
    }

    public InsightJpaEntity toJpaEntity(Insight domain, AnaliseIAJpaEntity analise) {
        InsightJpaEntity entity = new InsightJpaEntity();
        entity.setId(domain.getId());
        entity.setAnalise(analise);
        entity.setTipo(domain.getTipo());
        entity.setDescricao(domain.getDescricao());
        entity.setSeveridade(domain.getSeveridade());
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
