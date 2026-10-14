package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.analysis.domain.enums.*;
import com.mentoai.mentoaiapi.copilot.application.dto.*;
import com.mentoai.mentoaiapi.copilot.application.repository.ContextoCopilotoRepository;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.util.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ContextRetriever {
    private final ClienteRepository clientes;
    private final ContextoCopilotoRepository repository;
    private final CopilotContextProperties limits;

    public ContextRetriever(ClienteRepository clientes, ContextoCopilotoRepository repository,
                            CopilotContextProperties limits) {
        this.clientes = clientes;
        this.repository = repository;
        this.limits = limits;
    }

    @Transactional(readOnly = true)
    public DadosContextoCopiloto recuperar(Long clienteId, EnumSet<IntencaoCopiloto> intencoes) {
        var cliente = clientes.buscarPorId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));
        boolean geral = intencoes.contains(IntencaoCopiloto.GENERAL_CONTEXT);
        int maxReunioes = geral ? Math.min(3, limits.getReunioes()) : limits.getReunioes();
        var reunioes = repository.reunioes(clienteId, maxReunioes);
        if (intencoes.contains(IntencaoCopiloto.CLIENT_EVOLUTION)) {
            reunioes = new ArrayList<>(reunioes);
            Collections.reverse(reunioes);
        }
        var reuniaoIds = reunioes.stream().map(ContextoCopiloto.ReuniaoContexto::id).toList();
        EnumSet<TipoSinalComercial> sinais = EnumSet.noneOf(TipoSinalComercial.class);
        EnumSet<TipoInsight> insights = EnumSet.noneOf(TipoInsight.class);
        for (IntencaoCopiloto intent : intencoes) {
            switch (intent) {
                case CLIENT_RISK -> { sinais.addAll(List.of(TipoSinalComercial.RISCO_CHURN, TipoSinalComercial.DOR, TipoSinalComercial.OBJECAO)); insights.add(TipoInsight.RISCO); }
                case CLIENT_OPPORTUNITY -> { sinais.addAll(List.of(TipoSinalComercial.OPORTUNIDADE, TipoSinalComercial.NECESSIDADE, TipoSinalComercial.ORCAMENTO, TipoSinalComercial.PRAZO, TipoSinalComercial.MOMENTO_CLIENTE)); insights.add(TipoInsight.OPORTUNIDADE); }
                case COMPETITOR_ANALYSIS -> sinais.add(TipoSinalComercial.CONCORRENCIA);
                case OBJECTION_ANALYSIS -> { sinais.addAll(List.of(TipoSinalComercial.OBJECAO, TipoSinalComercial.DOR)); insights.add(TipoInsight.RISCO); }
                case CLIENT_EVOLUTION, CLIENT_SUMMARY, GENERAL_CONTEXT -> { sinais.addAll(EnumSet.allOf(TipoSinalComercial.class)); insights.addAll(EnumSet.allOf(TipoInsight.class)); }
            }
        }
        if (sinais.isEmpty()) sinais.addAll(EnumSet.allOf(TipoSinalComercial.class));
        if (insights.isEmpty()) insights.addAll(EnumSet.allOf(TipoInsight.class));
        int maxSinais = geral ? Math.min(4, limits.getSinais()) : limits.getSinais();
        int maxInsights = geral ? Math.min(3, limits.getInsights()) : limits.getInsights();
        var encontradosSinais = repository.sinais(clienteId, reuniaoIds, List.copyOf(sinais), maxSinais);
        var encontradosInsights = repository.insights(clienteId, reuniaoIds, List.copyOf(insights), maxInsights);
        var alertas = intencoes.contains(IntencaoCopiloto.CLIENT_RISK)
                ? repository.alertas(encontradosSinais.stream().map(ContextoCopiloto.SinalContexto::id).toList(), limits.getAlertas())
                : List.<ContextoCopiloto.AlertaContexto>of();
        return new DadosContextoCopiloto(cliente, reunioes, encontradosInsights, encontradosSinais, alertas);
    }
}
