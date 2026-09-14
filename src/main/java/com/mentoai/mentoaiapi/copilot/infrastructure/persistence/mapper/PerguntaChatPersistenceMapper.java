package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.PerguntaChatJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PerguntaChatPersistenceMapper {

    private final ChatPersistenceMapper chatMapper;

    public PerguntaChatPersistenceMapper(ChatPersistenceMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    public PerguntaChat toDomain(PerguntaChatJpaEntity entity) {
        return new PerguntaChat(entity.getId(), chatMapper.toDomain(entity.getChat()), entity.getPergunta(),
                entity.getResposta(), entity.getCriacao());
    }

    public PerguntaChatJpaEntity toJpaEntity(PerguntaChat domain, ChatJpaEntity chat) {
        PerguntaChatJpaEntity entity = new PerguntaChatJpaEntity();
        entity.setId(domain.getId());
        entity.setChat(chat);
        entity.setPergunta(domain.getPergunta());
        entity.setResposta(domain.getResposta());
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
