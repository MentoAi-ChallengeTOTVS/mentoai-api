package com.mentoai.mentoaiapi.meeting.presentation.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public record UploadTranscricaoRequest(
        @NotNull MultipartFile arquivo,
        @NotNull @Positive Long clienteId,
        @NotNull @Positive Long usuarioId,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataReuniao,
        @NotNull @PositiveOrZero Integer duracaoMinutos
) {
}
