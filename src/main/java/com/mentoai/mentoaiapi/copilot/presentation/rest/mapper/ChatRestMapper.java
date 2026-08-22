package com.mentoai.mentoaiapi.copilot.presentation.rest.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.presentation.rest.response.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatRestMapper {

    public ChatResponse toResponse(Chat chat) {
        return new ChatResponse(chat.getId(), chat.getTitulo(), chat.getUsuario().getId(), chat.getCriacao());
    }
}
