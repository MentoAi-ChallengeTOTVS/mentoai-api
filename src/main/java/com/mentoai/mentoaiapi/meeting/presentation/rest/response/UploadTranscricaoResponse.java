package com.mentoai.mentoaiapi.meeting.presentation.rest.response;

import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;

public record UploadTranscricaoResponse(
        Long reuniaoId,
        Long transcricaoId,
        Long analiseId,
        StatusProcessamento status
) {
}
