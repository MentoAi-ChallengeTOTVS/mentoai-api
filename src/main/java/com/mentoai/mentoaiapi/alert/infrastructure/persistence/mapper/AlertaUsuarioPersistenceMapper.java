package com.mentoai.mentoaiapi.alert.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaJpaEntity;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaUsuarioJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class AlertaUsuarioPersistenceMapper {

    private final AlertaPersistenceMapper alertaMapper;
    private final UsuarioPersistenceMapper usuarioMapper;

    public AlertaUsuarioPersistenceMapper(
            AlertaPersistenceMapper alertaMapper, UsuarioPersistenceMapper usuarioMapper) {
        this.alertaMapper = alertaMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public AlertaUsuario toDomain(AlertaUsuarioJpaEntity entity) {
        return new AlertaUsuario(entity.getId(), alertaMapper.toDomain(entity.getAlerta()),
                usuarioMapper.toDomain(entity.getUsuario()), Boolean.TRUE.equals(entity.getLido()),
                entity.getLidoEm());
    }

    public AlertaUsuarioJpaEntity toJpaEntity(
            AlertaUsuario domain, AlertaJpaEntity alerta, UsuarioJpaEntity usuario) {
        AlertaUsuarioJpaEntity entity = new AlertaUsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setAlerta(alerta);
        entity.setUsuario(usuario);
        entity.setLido(domain.isLido());
        entity.setLidoEm(domain.getLidoEm());
        return entity;
    }
}
