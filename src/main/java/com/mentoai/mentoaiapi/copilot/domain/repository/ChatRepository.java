package com.mentoai.mentoaiapi.copilot.domain.repository;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRepository {

    Chat salvar(Chat chat);
    Optional<Chat> buscarPorIdEUsuarioId(Long id, Long usuarioId);
    Page<Chat> listarPorUsuarioId(Long usuarioId, Pageable pageable);
}
