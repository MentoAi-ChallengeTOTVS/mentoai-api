package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(entity.getId(), entity.getNome(), entity.getSegmento(), entity.getPorte(),
                entity.getCriacao(), entity.getStatus());
    }

    public ClienteJpaEntity toJpaEntity(Cliente domain) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setSegmento(domain.getSegmento());
        entity.setPorte(domain.getPorte());
        entity.setCriacao(domain.getCriacao());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}
