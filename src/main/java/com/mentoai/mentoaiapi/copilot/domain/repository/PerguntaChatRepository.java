package com.mentoai.mentoaiapi.copilot.domain.repository;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import java.util.List;
import java.util.Optional;

public interface PerguntaChatRepository {

    PerguntaChat salvar(PerguntaChat perguntaChat);
    Optional<PerguntaChat> buscarPorId(Long id);
    List<PerguntaChat> buscarPorChatId(Long chatId);
}
