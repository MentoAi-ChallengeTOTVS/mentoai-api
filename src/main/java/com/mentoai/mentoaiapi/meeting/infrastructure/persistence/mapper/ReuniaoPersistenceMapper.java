package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class ReuniaoPersistenceMapper {

    private final ClientePersistenceMapper clienteMapper;
    private final UsuarioPersistenceMapper usuarioMapper;

    public ReuniaoPersistenceMapper(
            ClientePersistenceMapper clienteMapper, UsuarioPersistenceMapper usuarioMapper) {
        this.clienteMapper = clienteMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public Reuniao toDomain(ReuniaoJpaEntity entity) {
        return new Reuniao(entity.getId(), entity.getDataReuniao(), entity.getDuracaoMinutos(),
                clienteMapper.toDomain(entity.getCliente()), usuarioMapper.toDomain(entity.getUsuario()),
                entity.getCriacao());
    }

    public ReuniaoJpaEntity toJpaEntity(
            Reuniao domain, ClienteJpaEntity cliente, UsuarioJpaEntity usuario) {
        ReuniaoJpaEntity entity = new ReuniaoJpaEntity();
        entity.setId(domain.getId());
        entity.setDataReuniao(domain.getDataReuniao());
        entity.setDuracaoMinutos(domain.getDuracaoMinutos());
        entity.setCliente(cliente);
        entity.setUsuario(usuario);
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
