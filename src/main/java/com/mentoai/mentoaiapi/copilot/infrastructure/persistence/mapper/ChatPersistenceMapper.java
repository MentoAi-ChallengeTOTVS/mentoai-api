package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ClientePersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatPersistenceMapper {

    private final UsuarioPersistenceMapper usuarioMapper;
    private final ClientePersistenceMapper clienteMapper;

    public ChatPersistenceMapper(UsuarioPersistenceMapper usuarioMapper, ClientePersistenceMapper clienteMapper) {
        this.usuarioMapper = usuarioMapper;
        this.clienteMapper = clienteMapper;
    }

    public Chat toDomain(ChatJpaEntity entity) {
        return new Chat(entity.getId(), entity.getTitulo(), usuarioMapper.toDomain(entity.getUsuario()),
                entity.getCliente() == null ? null : clienteMapper.toDomain(entity.getCliente()),
                entity.getCriacao());
    }

    public ChatJpaEntity toJpaEntity(Chat domain, UsuarioJpaEntity usuario, ClienteJpaEntity cliente) {
        ChatJpaEntity entity = new ChatJpaEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setUsuario(usuario);
        entity.setCliente(cliente);
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
