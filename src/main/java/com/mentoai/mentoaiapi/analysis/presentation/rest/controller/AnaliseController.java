package com.mentoai.mentoaiapi.analysis.presentation.rest.controller;

import com.mentoai.mentoaiapi.analysis.application.service.AnaliseIAService;
import com.mentoai.mentoaiapi.analysis.presentation.rest.mapper.AnaliseIARestMapper;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.AnaliseIAResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analises")
public class AnaliseController {

    private final AnaliseIAService analiseService;
    private final AnaliseIARestMapper mapper;

    public AnaliseController(
            AnaliseIAService analiseService,
            AnaliseIARestMapper mapper) {
        this.analiseService = analiseService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnaliseIAResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(analiseService.buscarPorId(id)));
    }

    @GetMapping("/reuniao/{reuniaoId}")
    public ResponseEntity<AnaliseIAResponse> buscarPorReuniao(@PathVariable Long reuniaoId) {
        return ResponseEntity.ok(mapper.toResponse(analiseService.buscarPorReuniao(reuniaoId)));
    }
}
