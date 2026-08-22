package com.mentoai.mentoaiapi.copilot.presentation.rest.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.presentation.rest.response.PerguntaChatResponse;
import org.springframework.stereotype.Component;

@Component
public class PerguntaChatRestMapper {

    public PerguntaChatResponse toResponse(PerguntaChat pergunta) {
        return new PerguntaChatResponse(pergunta.getId(), pergunta.getChat().getId(), pergunta.getPergunta(),
                pergunta.getResposta(), pergunta.getCriacao());
    }
}
