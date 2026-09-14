package com.mentoai.mentoaiapi.alert.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.SinalComercialPersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class AlertaPersistenceMapper {

    private final SinalComercialPersistenceMapper sinalMapper;

    public AlertaPersistenceMapper(SinalComercialPersistenceMapper sinalMapper) {
        this.sinalMapper = sinalMapper;
    }

    public Alerta toDomain(AlertaJpaEntity entity) {
        return new Alerta(entity.getId(), sinalMapper.toDomain(entity.getSinalComercial()),
                entity.getPrioridade(), entity.getMotivo(), entity.getCriacao());
    }

    public AlertaJpaEntity toJpaEntity(Alerta domain, SinalComercialJpaEntity sinalComercial) {
        AlertaJpaEntity entity = new AlertaJpaEntity();
        entity.setId(domain.getId());
        entity.setSinalComercial(sinalComercial);
        entity.setPrioridade(domain.getPrioridade());
        entity.setMotivo(domain.getMotivo());
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
