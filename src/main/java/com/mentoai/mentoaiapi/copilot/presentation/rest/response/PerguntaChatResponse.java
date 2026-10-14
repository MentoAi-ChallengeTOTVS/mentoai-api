package com.mentoai.mentoaiapi.copilot.presentation.rest.response;

import java.time.LocalDateTime;
import java.util.List;
import com.mentoai.mentoaiapi.copilot.domain.entity.FonteCopiloto;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;

public record PerguntaChatResponse(
        Long id,
        String pergunta,
        String resposta,
        IntencaoCopiloto intencao,
        ClienteCopilotoResponse cliente,
        List<FonteCopiloto> fontes,
        LocalDateTime criacao
) {
}
