package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.repository.PerguntaChatRepository;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper.PerguntaChatPersistenceMapper;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository.SpringDataPerguntaChatRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Collections;

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
    public Page<PerguntaChat> listarPorChatId(Long chatId, Pageable pageable) {
        return repository.findByChat_Id(chatId, pageable).map(mapper::toDomain);
    }

    @Override
    public List<PerguntaChat> buscarHistoricoRecente(Long chatId, int limite) {
        Pageable pageable = PageRequest.of(0, limite, Sort.by(Sort.Order.desc("criacao"), Sort.Order.desc("id")));
        List<PerguntaChat> recentes = new java.util.ArrayList<>(repository
                .findByChat_IdAndRespostaIsNotNull(chatId, pageable).stream().map(mapper::toDomain).toList());
        Collections.reverse(recentes);
        return recentes;
    }
}
