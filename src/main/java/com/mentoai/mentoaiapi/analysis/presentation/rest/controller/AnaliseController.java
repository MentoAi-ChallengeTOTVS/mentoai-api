package com.mentoai.mentoaiapi.analysis.presentation.rest.controller;

import com.mentoai.mentoaiapi.analysis.application.service.AnaliseIAService;
import com.mentoai.mentoaiapi.analysis.application.service.InsightService;
import com.mentoai.mentoaiapi.analysis.application.service.SinalComercialService;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.analysis.presentation.rest.mapper.AnaliseIARestMapper;
import com.mentoai.mentoaiapi.analysis.presentation.rest.response.AnaliseIAResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analises")
public class AnaliseController {

    private final AnaliseIAService analiseService;
    private final InsightService insightService;
    private final SinalComercialService sinalService;
    private final AnaliseIARestMapper mapper;

    public AnaliseController(
            AnaliseIAService analiseService,
            InsightService insightService,
            SinalComercialService sinalService,
            AnaliseIARestMapper mapper) {
        this.analiseService = analiseService;
        this.insightService = insightService;
        this.sinalService = sinalService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnaliseIAResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(analiseService.buscarPorId(id)));
    }

    @GetMapping("/reuniao/{reuniaoId}")
    public ResponseEntity<AnaliseIAResponse> buscarPorReuniao(@PathVariable Long reuniaoId) {
        return ResponseEntity.ok(toResponse(analiseService.buscarPorReuniao(reuniaoId)));
    }

    private AnaliseIAResponse toResponse(AnaliseIA analise) {
        if (analise.getStatusProcessamento() != StatusProcessamento.PROCESSADA) {
            return mapper.toResponse(analise, List.of(), List.of());
        }
        return mapper.toResponse(analise,
                insightService.listarPorAnalise(analise.getId()),
                sinalService.listarPorAnalise(analise.getId()));
    }
}
