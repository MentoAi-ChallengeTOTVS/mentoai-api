package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.service.ReuniaoService;
import com.mentoai.mentoaiapi.meeting.application.service.TranscricaoService;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.ReuniaoRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.TranscricaoRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ReuniaoResponse;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.TranscricaoResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reunioes")
public class ReuniaoController {

    private final ReuniaoService reuniaoService;
    private final TranscricaoService transcricaoService;
    private final ReuniaoRestMapper reuniaoMapper;
    private final TranscricaoRestMapper transcricaoMapper;

    public ReuniaoController(
            ReuniaoService reuniaoService,
            TranscricaoService transcricaoService,
            ReuniaoRestMapper reuniaoMapper,
            TranscricaoRestMapper transcricaoMapper) {
        this.reuniaoService = reuniaoService;
        this.transcricaoService = transcricaoService;
        this.reuniaoMapper = reuniaoMapper;
        this.transcricaoMapper = transcricaoMapper;
    }

    @GetMapping
    public ResponseEntity<List<ReuniaoResponse>> listar() {
        return ResponseEntity.ok(reuniaoService.listar().stream().map(reuniaoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReuniaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reuniaoMapper.toResponse(reuniaoService.buscarPorId(id)));
    }

    @GetMapping("/{id}/transcricao")
    public ResponseEntity<TranscricaoResponse> buscarTranscricao(@PathVariable Long id) {
        return ResponseEntity.ok(transcricaoMapper.toResponse(transcricaoService.buscarPorReuniao(id)));
    }
}
