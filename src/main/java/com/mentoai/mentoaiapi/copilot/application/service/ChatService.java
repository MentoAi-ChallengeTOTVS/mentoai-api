package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.repository.ChatRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.copilot.domain.repository.PerguntaChatRepository;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.shared.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PerguntaChatRepository perguntaRepository;

    public ChatService(ChatRepository chatRepository, UsuarioRepository usuarioRepository,
                       ClienteRepository clienteRepository, PerguntaChatRepository perguntaRepository) {
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.perguntaRepository = perguntaRepository;
    }

    @Transactional
    public Chat criar(String titulo, Long clienteId, String email) {
        Usuario usuario = usuarioAutenticado(email);
        var cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));
        return chatRepository.salvar(new Chat(null, titulo.trim(), usuario, cliente, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public Chat buscarPorId(Long id, String email) {
        Long usuarioId = usuarioAutenticado(email).getId();
        return chatRepository.buscarPorIdEUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Chat> listar(String email, int page, int size) {
        Long usuarioId = usuarioAutenticado(email).getId();
        return chatRepository.listarPorUsuarioId(usuarioId, PageRequest.of(page, size,
                Sort.by(Sort.Order.desc("criacao"), Sort.Order.desc("id"))));
    }

    @Transactional(readOnly = true)
    public Page<PerguntaChat> historico(Long chatId, String email, int page, int size) {
        buscarPorId(chatId, email);
        return perguntaRepository.listarPorChatId(chatId, PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("criacao"), Sort.Order.asc("id"))));
    }

    private Usuario usuarioAutenticado(String email) {
        if (email == null || email.isBlank()) throw new BusinessException("Usuário não autenticado");
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
    }
}
