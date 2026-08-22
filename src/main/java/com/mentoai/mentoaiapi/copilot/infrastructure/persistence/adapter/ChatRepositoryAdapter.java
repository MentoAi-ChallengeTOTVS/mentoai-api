package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.repository.ChatRepository;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper.ChatPersistenceMapper;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository.SpringDataChatRepository;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

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
        return mapper.toDomain(repository.save(mapper.toJpaEntity(chat, usuario)));
    }

    @Override
    public Optional<Chat> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Chat> buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuario_Id(usuarioId).stream().map(mapper::toDomain).toList();
    }
}
