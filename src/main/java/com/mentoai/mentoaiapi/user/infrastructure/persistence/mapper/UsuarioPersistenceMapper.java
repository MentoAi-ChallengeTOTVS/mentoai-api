package com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {

    public Usuario toDomain(UsuarioJpaEntity entity) {
        return new Usuario(entity.getId(), entity.getNome(), entity.getEmail(), entity.getSenha(),
                entity.getPerfil(), entity.getAtivo(), entity.getCriacao(), entity.getAtualizacao());
    }

    public UsuarioJpaEntity toJpaEntity(Usuario domain) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setSenha(domain.getSenha());
        entity.setPerfil(domain.getPerfil());
        entity.setAtivo(domain.getAtivo());
        entity.setCriacao(domain.getCriacao());
        entity.setAtualizacao(domain.getAtualizacao());
        return entity;
    }
}
