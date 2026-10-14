package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.repository.PerguntaChatRepository;
import com.mentoai.mentoaiapi.copilot.domain.entity.FonteCopiloto;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerguntaChatService {

    private final PerguntaChatRepository perguntaRepository;
    public PerguntaChatService(PerguntaChatRepository perguntaRepository) {
        this.perguntaRepository = perguntaRepository;
    }

    @Transactional
    public PerguntaChat salvarConcluida(Chat chat, String pergunta, String resposta,
                                        IntencaoCopiloto intencao, List<FonteCopiloto> fontes) {
        return perguntaRepository.salvar(new PerguntaChat(null, chat, pergunta, resposta,
                intencao, fontes, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public List<PerguntaChat> historicoRecente(Long chatId, int limite) {
        return perguntaRepository.buscarHistoricoRecente(chatId, limite);
    }
}
