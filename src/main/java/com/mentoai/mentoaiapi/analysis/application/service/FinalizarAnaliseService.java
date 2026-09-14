package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.alert.application.service.AlertaUsuarioService;
import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaRepository;
import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinalizarAnaliseService {

    private static final Set<TipoSinalComercial> TIPOS_COM_ALERTA = Set.of(
            TipoSinalComercial.RISCO_CHURN,
            TipoSinalComercial.CONCORRENCIA,
            TipoSinalComercial.OBJECAO);

    private final AnaliseIAService analiseIAService;
    private final InsightService insightService;
    private final SinalComercialService sinalComercialService;
    private final AlertaRepository alertaRepository;
    private final AlertaUsuarioService alertaUsuarioService;

    public FinalizarAnaliseService(
            AnaliseIAService analiseIAService,
            InsightService insightService,
            SinalComercialService sinalComercialService,
            AlertaRepository alertaRepository,
            AlertaUsuarioService alertaUsuarioService) {
        this.analiseIAService = analiseIAService;
        this.insightService = insightService;
        this.sinalComercialService = sinalComercialService;
        this.alertaRepository = alertaRepository;
        this.alertaUsuarioService = alertaUsuarioService;
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

        List<SinalComercial> sinaisSalvos = sinalComercialService.salvarTodos(
                resultado.sinaisComerciais().stream()
                        .map(sinal -> new SinalComercial(
                                null, analise, sinal.tipo(), sinal.descricao(), sinal.evidencia(), sinal.relevancia(), criacao))
                        .toList());

        // Geração Automática de Alertas (F04)
        gerarAlertasAutomaticos(sinaisSalvos, analise);

        return analise;
    }

    private void gerarAlertasAutomaticos(List<SinalComercial> sinais, AnaliseIA analise) {
        for (SinalComercial sinal : sinais) {
            if (!deveGerarAlerta(sinal)) {
                continue;
            }

            Alerta alerta = new Alerta(
                    null,
                    sinal,
                    PrioridadeAlerta.ALTA,
                    sinal.getDescricao(),
                    LocalDateTime.now()
            );

            Alerta alertaSalvo = alertaRepository.salvar(alerta);

            // Vincula ao usuário caso a reunião possua usuário associado
            Long usuarioId = extrairUsuarioId(analise);
            if (usuarioId != null) {
                alertaUsuarioService.registrar(alertaSalvo.getId(), usuarioId);
            }
        }
    }

    static boolean deveGerarAlerta(SinalComercial sinal) {
        return sinal != null
                && sinal.getRelevancia() == RelevanciaSinal.ALTA
                && sinal.getTipo() != null
                && TIPOS_COM_ALERTA.contains(sinal.getTipo());
    }

    private Long extrairUsuarioId(AnaliseIA analise) {
        if (analise.getReuniao() != null && analise.getReuniao().getUsuario() != null) {
            return analise.getReuniao().getUsuario().getId();
        }
        return null; // ou defina um ID padrão/log de fallback
    }
}
