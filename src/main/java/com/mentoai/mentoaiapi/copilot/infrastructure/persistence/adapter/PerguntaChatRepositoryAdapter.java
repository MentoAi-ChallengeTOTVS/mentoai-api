package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.repository.PerguntaChatRepository;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper.PerguntaChatPersistenceMapper;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository.SpringDataPerguntaChatRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PerguntaChatRepositoryAdapter implements PerguntaChatRepository {

    private final SpringDataPerguntaChatRepository repository;
    private final PerguntaChatPersistenceMapper mapper;
    private final EntityManager entityManager;

    public PerguntaChatRepositoryAdapter(
            SpringDataPerguntaChatRepository repository,
            PerguntaChatPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public PerguntaChat salvar(PerguntaChat perguntaChat) {
        ChatJpaEntity chat = entityManager.getReference(ChatJpaEntity.class, perguntaChat.getChat().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(perguntaChat, chat)));
    }

    @Override
    public Optional<PerguntaChat> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PerguntaChat> buscarPorChatId(Long chatId) {
        return repository.findByChat_Id(chatId).stream().map(mapper::toDomain).toList();
    }
}
