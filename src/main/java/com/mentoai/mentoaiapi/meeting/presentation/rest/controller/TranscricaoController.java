package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.dto.UploadTranscricaoResult;
import com.mentoai.mentoaiapi.meeting.application.service.UploadAnaliseService;
import com.mentoai.mentoaiapi.meeting.application.service.TranscricaoService;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.UploadTranscricaoRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.TranscricaoRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.TranscricaoResponse;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.UploadTranscricaoResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transcricoes")
public class TranscricaoController {

    private final UploadAnaliseService uploadAnaliseService;
    private final TranscricaoService transcricaoService;
    private final TranscricaoRestMapper transcricaoMapper;

    public TranscricaoController(
            UploadAnaliseService uploadAnaliseService,
            TranscricaoService transcricaoService,
            TranscricaoRestMapper transcricaoMapper) {
        this.uploadAnaliseService = uploadAnaliseService;
        this.transcricaoService = transcricaoService;
        this.transcricaoMapper = transcricaoMapper;
    }

    @GetMapping
    public ResponseEntity<List<TranscricaoResponse>> listar() {
        return ResponseEntity.ok(
                transcricaoService.listar().stream().map(transcricaoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranscricaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transcricaoMapper.toResponse(transcricaoService.buscarPorId(id)));
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadTranscricaoResponse> upload(
            @Valid @ModelAttribute UploadTranscricaoRequest request) {
        byte[] arquivo;
        try {
            arquivo = request.arquivo().getBytes();
        } catch (IOException exception) {
            throw new UncheckedIOException("Falha ao ler o arquivo enviado", exception);
        }

        UploadTranscricaoResult result = uploadAnaliseService.executar(
                request.arquivo().getOriginalFilename(),
                arquivo,
                request.clienteId(),
                request.usuarioId(),
                request.dataReuniao(),
                request.duracaoMinutos());
        UploadTranscricaoResponse response = new UploadTranscricaoResponse(
                result.reuniaoId(),
                result.transcricaoId(),
                result.analiseId(),
                result.status());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
