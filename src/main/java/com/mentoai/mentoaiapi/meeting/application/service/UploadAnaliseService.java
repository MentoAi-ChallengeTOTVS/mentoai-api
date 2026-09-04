package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.analysis.application.event.AnaliseSolicitadaEvent;
import com.mentoai.mentoaiapi.meeting.application.dto.UploadTranscricaoResult;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UploadAnaliseService {

    private final UploadTranscricaoService uploadTranscricaoService;
    private final ApplicationEventPublisher eventPublisher;

    public UploadAnaliseService(
            UploadTranscricaoService uploadTranscricaoService,
            ApplicationEventPublisher eventPublisher) {
        this.uploadTranscricaoService = uploadTranscricaoService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UploadTranscricaoResult executar(
            String nomeOriginalArquivo,
            byte[] arquivo,
            Long clienteId,
            Long usuarioId,
            LocalDateTime dataReuniao,
            Integer duracaoMinutos) {
        UploadTranscricaoResult upload = uploadTranscricaoService.executar(
                nomeOriginalArquivo, arquivo, clienteId, usuarioId, dataReuniao, duracaoMinutos);

        // O upload participa desta transação por REQUIRED; o listener só executa após o commit do orquestrador.
        eventPublisher.publishEvent(new AnaliseSolicitadaEvent(upload.analiseId()));
        return upload;
    }
}
