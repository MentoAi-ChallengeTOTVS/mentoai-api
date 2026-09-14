package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.application.ai.ResumoContextualPrompt;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.domain.entity.ResumoReuniaoRecente;
import com.mentoai.mentoaiapi.meeting.application.service.ClienteService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsolidarContextoClienteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsolidarContextoClienteService.class);
    private final ClienteService clienteService;
    private final AnaliseIAService analiseService;
    private final AiProvider aiProvider;
    private final ResumoContextualPrompt prompt;

    public ConsolidarContextoClienteService(ClienteService clienteService, AnaliseIAService analiseService,
            AiProvider aiProvider, ResumoContextualPrompt prompt) {
        this.clienteService = clienteService;
        this.analiseService = analiseService;
        this.aiProvider = aiProvider;
        this.prompt = prompt;
    }

    @Transactional(propagation = Propagation.NEVER)
    public void consolidar(Long clienteId, Long analiseId) {
        String anterior = null;
        try {
            anterior = clienteService.buscarResumoContextual(clienteId);
            List<ResumoReuniaoRecente> reunioes = analiseService.buscarResumosRecentesPorCliente(clienteId);
            if (reunioes.isEmpty()) {
                throw new IllegalStateException("Não há resumos executivos válidos para consolidação");
            }
            String novo = prompt.validarResposta(aiProvider.gerar(prompt.criarRequisicao(anterior, reunioes)));
            clienteService.atualizarResumoContextual(clienteId, novo);
        } catch (RuntimeException exception) {
            String mensagem = anterior == null
                    ? "Não foi possível criar o resumo contextual do cliente."
                    : "Não foi possível atualizar o resumo contextual do cliente; memória anterior preservada.";
            LOGGER.warn("{} clienteId={}, analiseId={}, falha={}",
                    mensagem, clienteId, analiseId, exception.getClass().getSimpleName());
            LOGGER.debug("Detalhes da falha contextual: clienteId={}, analiseId={}", clienteId, analiseId, exception);
        }
    }
}
