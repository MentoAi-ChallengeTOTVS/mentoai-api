package com.mentoai.mentoaiapi.copilot.presentation.rest.controller;

import com.mentoai.mentoaiapi.copilot.application.service.ChatService;
import com.mentoai.mentoaiapi.copilot.application.service.PerguntaChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;
    private final PerguntaChatService perguntaChatService;

    public ChatController(ChatService chatService, PerguntaChatService perguntaChatService) {
        this.chatService = chatService;
        this.perguntaChatService = perguntaChatService;
    }
}
