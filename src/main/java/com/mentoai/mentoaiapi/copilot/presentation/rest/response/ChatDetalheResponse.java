package com.mentoai.mentoaiapi.copilot.presentation.rest.response;

import org.springframework.data.domain.Page;

public record ChatDetalheResponse(ChatResponse chat, Page<PerguntaChatResponse> mensagens) {}
