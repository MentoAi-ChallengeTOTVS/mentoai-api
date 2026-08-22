package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatPersistenceMapper {

    private final UsuarioPersistenceMapper usuarioMapper;

    public ChatPersistenceMapper(UsuarioPersistenceMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public Chat toDomain(ChatJpaEntity entity) {
        return new Chat(entity.getId(), entity.getTitulo(), usuarioMapper.toDomain(entity.getUsuario()),
                entity.getCriacao());
    }

    public ChatJpaEntity toJpaEntity(Chat domain, UsuarioJpaEntity usuario) {
        ChatJpaEntity entity = new ChatJpaEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setUsuario(usuario);
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
