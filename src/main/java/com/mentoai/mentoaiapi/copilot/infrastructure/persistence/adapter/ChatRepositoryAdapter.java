package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.repository.ChatRepository;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper.ChatPersistenceMapper;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository.SpringDataChatRepository;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public class ChatRepositoryAdapter implements ChatRepository {

    private final SpringDataChatRepository repository;
    private final ChatPersistenceMapper mapper;
    private final EntityManager entityManager;

    public ChatRepositoryAdapter(
            SpringDataChatRepository repository, ChatPersistenceMapper mapper, EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Chat salvar(Chat chat) {
        UsuarioJpaEntity usuario = entityManager.getReference(UsuarioJpaEntity.class, chat.getUsuario().getId());
        ClienteJpaEntity cliente = chat.getCliente() == null ? null :
                entityManager.getReference(ClienteJpaEntity.class, chat.getCliente().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(chat, usuario, cliente)));
    }

    @Override
    public Optional<Chat> buscarPorIdEUsuarioId(Long id, Long usuarioId) {
        return repository.findByIdAndUsuario_Id(id, usuarioId).map(mapper::toDomain);
    }

    @Override
    public Page<Chat> listarPorUsuarioId(Long usuarioId, Pageable pageable) {
        return repository.findByUsuario_Id(usuarioId, pageable).map(mapper::toDomain);
    }
}
