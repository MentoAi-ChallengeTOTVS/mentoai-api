package com.mentoai.mentoaiapi.meeting.application.dto;

import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;

public record UploadTranscricaoResult(
        Long reuniaoId,
        Long transcricaoId,
        Long analiseId,
        StatusProcessamento status
) {
}
