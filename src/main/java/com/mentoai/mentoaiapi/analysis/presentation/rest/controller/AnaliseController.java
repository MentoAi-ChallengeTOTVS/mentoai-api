package com.mentoai.mentoaiapi.analysis.presentation.rest.controller;

import com.mentoai.mentoaiapi.analysis.application.service.AnaliseIAService;
import com.mentoai.mentoaiapi.analysis.application.service.InsightService;
import com.mentoai.mentoaiapi.analysis.application.service.SinalComercialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analises")
public class AnaliseController {

    private final AnaliseIAService analiseService;
    private final InsightService insightService;
    private final SinalComercialService sinalComercialService;

    public AnaliseController(
            AnaliseIAService analiseService,
            InsightService insightService,
            SinalComercialService sinalComercialService) {
        this.analiseService = analiseService;
        this.insightService = insightService;
        this.sinalComercialService = sinalComercialService;
    }
}
