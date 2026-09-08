package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.application.dto.EntradaAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.meeting.application.service.TranscricaoService;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessarAnaliseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessarAnaliseService.class);
    private static final String MENSAGEM_ERRO = "Não foi possível concluir o processamento da análise.";

    private final AnaliseIAService analiseIAService;
    private final TranscricaoService transcricaoService;
    private final GerarAnaliseAiService gerarAnaliseAiService;
    private final FinalizarAnaliseService finalizarAnaliseService;
    private final ConsolidarContextoClienteService consolidarContextoClienteService;

    public ProcessarAnaliseService(
            AnaliseIAService analiseIAService,
            TranscricaoService transcricaoService,
            GerarAnaliseAiService gerarAnaliseAiService,
            FinalizarAnaliseService finalizarAnaliseService,
            ConsolidarContextoClienteService consolidarContextoClienteService) {
        this.analiseIAService = analiseIAService;
        this.transcricaoService = transcricaoService;
        this.gerarAnaliseAiService = gerarAnaliseAiService;
        this.finalizarAnaliseService = finalizarAnaliseService;
        this.consolidarContextoClienteService = consolidarContextoClienteService;
    }

    @Transactional(propagation = Propagation.NEVER)
    public AnaliseIA processar(Long analiseId) {
        AnaliseIA analise = analiseIAService.iniciarProcessamento(analiseId);
        AnaliseIA finalizada;
        try {
            Reuniao reuniao = analise.getReuniao();
            Cliente cliente = reuniao.getCliente();
            Transcricao transcricao = transcricaoService.buscarPorReuniao(reuniao.getId());
            EntradaAnaliseAi entrada = new EntradaAnaliseAi(
                    new EntradaAnaliseAi.ClienteContexto(
                            cliente.getNome(), cliente.getSegmento(), cliente.getPorte()),
                    new EntradaAnaliseAi.TranscricaoConteudo(transcricao.getConteudo()));

            ResultadoAnaliseAi resultado = gerarAnaliseAiService.gerar(entrada);
            finalizada = finalizarAnaliseService.finalizar(analiseId, resultado);
        } catch (RuntimeException falhaOriginal) {
            LOGGER.error("Falha no processamento da análise {}", analiseId, falhaOriginal);
            finalizada = tratarFalha(analiseId, falhaOriginal);
        }
        if (finalizada.getStatusProcessamento() == StatusProcessamento.PROCESSADA) {
            // O proxy de finalizar() já concluiu o commit. Nunca incluir esta etapa no catch crítico.
            try {
                consolidarContextoClienteService.consolidar(finalizada.getReuniao().getCliente().getId(), analiseId);
            } catch (RuntimeException exception) {
                // Protege também contra falhas no proxy/interceptadores do pós-processamento.
                LOGGER.warn("Falha no pós-processamento contextual da análise {}", analiseId, exception);
            }
        }
        return finalizada;
    }

    private AnaliseIA tratarFalha(Long analiseId, RuntimeException falhaOriginal) {
        try {
            AnaliseIA atual = analiseIAService.buscarPorId(analiseId);
            // O commit pode ter ocorrido antes de uma exceção no retorno da finalização.
            if (atual.getStatusProcessamento() == StatusProcessamento.PROCESSADA) {
                return atual;
            }
            if (atual.getStatusProcessamento() == StatusProcessamento.PROCESSANDO) {
                return analiseIAService.registrarFalha(analiseId, MENSAGEM_ERRO);
            }
        } catch (RuntimeException falhaAoRegistrarErro) {
            if (falhaAoRegistrarErro != falhaOriginal) {
                falhaOriginal.addSuppressed(falhaAoRegistrarErro);
            }
        }
        throw falhaOriginal;
    }
}
