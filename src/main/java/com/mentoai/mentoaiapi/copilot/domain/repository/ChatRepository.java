package com.mentoai.mentoaiapi.copilot.domain.repository;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Chat salvar(Chat chat);
    Optional<Chat> buscarPorId(Long id);
    List<Chat> buscarPorUsuarioId(Long usuarioId);
}
