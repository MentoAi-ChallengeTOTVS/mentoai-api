package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinalizarAnaliseService {

    private final AnaliseIAService analiseIAService;
    private final InsightService insightService;
    private final SinalComercialService sinalComercialService;

    public FinalizarAnaliseService(
            AnaliseIAService analiseIAService,
            InsightService insightService,
            SinalComercialService sinalComercialService) {
        this.analiseIAService = analiseIAService;
        this.insightService = insightService;
        this.sinalComercialService = sinalComercialService;
    }

    @Transactional
    public AnaliseIA finalizar(Long analiseId, ResultadoAnaliseAi resultado) {
        Objects.requireNonNull(resultado, "O resultado da análise é obrigatório");
        AnaliseIA analise = analiseIAService.concluir(
                analiseId, resultado.resumoExecutivo(), resultado.sentimentoGeral());
        LocalDateTime criacao = LocalDateTime.now();

        insightService.salvarTodos(resultado.insights().stream()
                .map(insight -> new Insight(
                        null, analise, insight.tipo(), insight.descricao(), insight.severidade(), criacao))
                .toList());
        sinalComercialService.salvarTodos(resultado.sinaisComerciais().stream()
                .map(sinal -> new SinalComercial(
                        null, analise, sinal.tipo(), sinal.descricao(), sinal.evidencia(), sinal.relevancia(), criacao))
                .toList());

        return analise;
    }
}
