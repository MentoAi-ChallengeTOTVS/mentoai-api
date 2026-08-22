package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.repository.ChatRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;

    public ChatService(ChatRepository chatRepository, UsuarioRepository usuarioRepository) {
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Chat criar(String titulo, Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + usuarioId));
        return chatRepository.salvar(new Chat(null, titulo, usuario, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public Chat buscarPorId(Long id) {
        return chatRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Chat> listarPorUsuario(Long usuarioId) {
        return chatRepository.buscarPorUsuarioId(usuarioId);
    }
}
