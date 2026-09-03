package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.analysis.application.service.ProcessarAnaliseService;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.meeting.application.dto.UploadTranscricaoResult;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class UploadAnaliseService {

    private final UploadTranscricaoService uploadTranscricaoService;
    private final ProcessarAnaliseService processarAnaliseService;

    public UploadAnaliseService(
            UploadTranscricaoService uploadTranscricaoService,
            ProcessarAnaliseService processarAnaliseService) {
        this.uploadTranscricaoService = uploadTranscricaoService;
        this.processarAnaliseService = processarAnaliseService;
    }

    public UploadTranscricaoResult executar(
            String nomeOriginalArquivo,
            byte[] arquivo,
            Long clienteId,
            Long usuarioId,
            LocalDateTime dataReuniao,
            Integer duracaoMinutos) {
        UploadTranscricaoResult upload = uploadTranscricaoService.executar(
                nomeOriginalArquivo, arquivo, clienteId, usuarioId, dataReuniao, duracaoMinutos);

        // O bean de upload conclui sua transação antes de retornar para este orquestrador.
        AnaliseIA analise = processarAnaliseService.processar(upload.analiseId());
        return new UploadTranscricaoResult(
                upload.reuniaoId(), upload.transcricaoId(), upload.analiseId(), analise.getStatusProcessamento());
    }
}
