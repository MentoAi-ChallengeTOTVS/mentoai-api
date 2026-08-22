package com.mentoai.mentoaiapi.meeting.presentation.rest.response;

import java.time.LocalDateTime;

public record TranscricaoResponse(
        Long id,
        String conteudo,
        String nomeArquivo,
        String formatoArquivo,
        String idioma,
        Long reuniaoId,
        LocalDateTime criacao
) {
}
