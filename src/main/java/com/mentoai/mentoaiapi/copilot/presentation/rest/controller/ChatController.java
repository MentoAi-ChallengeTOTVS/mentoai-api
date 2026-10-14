package com.mentoai.mentoaiapi.copilot.presentation.rest.controller;

import com.mentoai.mentoaiapi.copilot.application.service.ChatService;
import com.mentoai.mentoaiapi.copilot.application.service.CopilotService;
import com.mentoai.mentoaiapi.copilot.presentation.rest.mapper.ChatRestMapper;
import com.mentoai.mentoaiapi.copilot.presentation.rest.mapper.PerguntaChatRestMapper;
import com.mentoai.mentoaiapi.copilot.presentation.rest.request.CriarChatRequest;
import com.mentoai.mentoaiapi.copilot.presentation.rest.request.CriarPerguntaChatRequest;
import com.mentoai.mentoaiapi.copilot.presentation.rest.response.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/copilot/chats")
public class ChatController {

    private final ChatService chatService;
    private final CopilotService copilotService;
    private final ChatRestMapper chatMapper;
    private final PerguntaChatRestMapper perguntaMapper;

    public ChatController(ChatService chatService, CopilotService copilotService,
                          ChatRestMapper chatMapper, PerguntaChatRestMapper perguntaMapper) {
        this.chatService = chatService;
        this.copilotService = copilotService;
        this.chatMapper = chatMapper;
        this.perguntaMapper = perguntaMapper;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> criar(@Valid @RequestBody CriarChatRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatMapper.toResponse(chatService.criar(request.titulo(), request.clienteId(), auth.getName())));
    }

    @GetMapping
    public ResponseEntity<Page<ChatResponse>> listar(Authentication auth,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return ResponseEntity.ok(chatService.listar(auth.getName(), page, size).map(chatMapper::toResponse));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDetalheResponse> detalhe(Authentication auth,
            @PathVariable @Positive Long chatId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size) {
        var chat = chatService.buscarPorId(chatId, auth.getName());
        var mensagens = chatService.historico(chatId, auth.getName(), page, size).map(perguntaMapper::toResponse);
        return ResponseEntity.ok(new ChatDetalheResponse(chatMapper.toResponse(chat), mensagens));
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<PerguntaChatResponse> perguntar(Authentication auth, @PathVariable @Positive Long chatId,
            @Valid @RequestBody CriarPerguntaChatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(perguntaMapper.toResponse(copilotService.perguntar(chatId, auth.getName(), request.pergunta())));
    }
}
