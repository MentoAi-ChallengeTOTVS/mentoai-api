package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.repository.ChatRepository;
import com.mentoai.mentoaiapi.copilot.domain.repository.PerguntaChatRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerguntaChatService {

    private final PerguntaChatRepository perguntaRepository;
    private final ChatRepository chatRepository;

    public PerguntaChatService(PerguntaChatRepository perguntaRepository, ChatRepository chatRepository) {
        this.perguntaRepository = perguntaRepository;
        this.chatRepository = chatRepository;
    }

    @Transactional
    public PerguntaChat registrar(String pergunta, Long chatId) {
        Chat chat = chatRepository.buscarPorId(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado: " + chatId));
        return perguntaRepository.salvar(new PerguntaChat(null, chat, pergunta, null, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public PerguntaChat buscarPorId(Long id) {
        return perguntaRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pergunta de chat não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<PerguntaChat> listarPorChat(Long chatId) {
        return perguntaRepository.buscarPorChatId(chatId);
    }
}
