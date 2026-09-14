package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SinalComercialPersistenceMapper {

    private final AnaliseIAPersistenceMapper analiseMapper;

    public SinalComercialPersistenceMapper(AnaliseIAPersistenceMapper analiseMapper) {
        this.analiseMapper = analiseMapper;
    }

    public SinalComercial toDomain(SinalComercialJpaEntity entity) {
        return new SinalComercial(entity.getId(), analiseMapper.toDomain(entity.getAnalise()), entity.getTipo(),
                entity.getDescricao(), entity.getEvidencia(), entity.getRelevancia(), entity.getCriacao());
    }

    public SinalComercialJpaEntity toJpaEntity(SinalComercial domain, AnaliseIAJpaEntity analise) {
        SinalComercialJpaEntity entity = new SinalComercialJpaEntity();
        entity.setId(domain.getId());
        entity.setAnalise(analise);
        entity.setTipo(domain.getTipo());
        entity.setDescricao(domain.getDescricao());
        entity.setEvidencia(domain.getEvidencia());
        entity.setRelevancia(domain.getRelevancia());
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
