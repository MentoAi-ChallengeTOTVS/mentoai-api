package com.mentoai.mentoaiapi.copilot.presentation.rest.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.presentation.rest.response.PerguntaChatResponse;
import com.mentoai.mentoaiapi.copilot.presentation.rest.response.ClienteCopilotoResponse;
import org.springframework.stereotype.Component;

@Component
public class PerguntaChatRestMapper {

    public PerguntaChatResponse toResponse(PerguntaChat pergunta) {
        var cliente = pergunta.getChat().getCliente();
        return new PerguntaChatResponse(pergunta.getId(), pergunta.getPergunta(), pergunta.getResposta(),
                pergunta.getIntencao(), cliente == null ? null : new ClienteCopilotoResponse(cliente.getId(), cliente.getNome()),
                pergunta.getFontes(), pergunta.getCriacao());
    }
}
