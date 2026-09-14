package com.mentoai.mentoaiapi.copilot.presentation.rest.response;

import java.time.LocalDateTime;

public record PerguntaChatResponse(
        Long id,
        Long chatId,
        String pergunta,
        String resposta,
        LocalDateTime criacao
) {
}
