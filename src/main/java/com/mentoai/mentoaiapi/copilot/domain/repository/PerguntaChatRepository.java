package com.mentoai.mentoaiapi.copilot.domain.repository;

import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerguntaChatRepository {

    PerguntaChat salvar(PerguntaChat perguntaChat);
    Page<PerguntaChat> listarPorChatId(Long chatId, Pageable pageable);
    List<PerguntaChat> buscarHistoricoRecente(Long chatId, int limite);
}
