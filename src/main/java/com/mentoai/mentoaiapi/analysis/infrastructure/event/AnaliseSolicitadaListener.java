package com.mentoai.mentoaiapi.analysis.infrastructure.event;

import com.mentoai.mentoaiapi.analysis.application.event.AnaliseSolicitadaEvent;
import com.mentoai.mentoaiapi.analysis.application.service.ProcessarAnaliseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnaliseSolicitadaListener {

    private final ProcessarAnaliseService processarAnaliseService;

    public AnaliseSolicitadaListener(ProcessarAnaliseService processarAnaliseService) {
        this.processarAnaliseService = processarAnaliseService;
    }

    @Async("analysisExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = false)
    public void aoSolicitarAnalise(AnaliseSolicitadaEvent event) {
        processarAnaliseService.processar(event.analiseId());
    }
}
