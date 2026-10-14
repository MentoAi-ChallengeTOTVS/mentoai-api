package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.entity.FonteCopiloto;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.PerguntaChatJpaEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import tools.jackson.databind.json.JsonMapper;

@Component
public class PerguntaChatPersistenceMapper {

    private final ChatPersistenceMapper chatMapper;
    private final JsonMapper json = JsonMapper.builder().build();

    public PerguntaChatPersistenceMapper(ChatPersistenceMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    public PerguntaChat toDomain(PerguntaChatJpaEntity entity) {
        List<FonteCopiloto> fontes = List.of();
        if (entity.getFontesJson() != null) {
            FontesPersistidas persisted = json.readValue(entity.getFontesJson(), FontesPersistidas.class);
            if (persisted.versao() != 1) {
                throw new IllegalStateException("Versão de fontes não suportada");
            }
            fontes = persisted.fontes();
        }
        return new PerguntaChat(entity.getId(), chatMapper.toDomain(entity.getChat()), entity.getPergunta(),
                entity.getResposta(), entity.getIntencao(), fontes, entity.getCriacao());
    }

    public PerguntaChatJpaEntity toJpaEntity(PerguntaChat domain, ChatJpaEntity chat) {
        PerguntaChatJpaEntity entity = new PerguntaChatJpaEntity();
        entity.setId(domain.getId());
        entity.setChat(chat);
        entity.setPergunta(domain.getPergunta());
        entity.setResposta(domain.getResposta());
        entity.setIntencao(domain.getIntencao());
        entity.setFontesJson(json.writeValueAsString(new FontesPersistidas(1, domain.getFontes())));
        entity.setCriacao(domain.getCriacao());
        return entity;
    }

    private record FontesPersistidas(int versao, List<FonteCopiloto> fontes) {}
}
