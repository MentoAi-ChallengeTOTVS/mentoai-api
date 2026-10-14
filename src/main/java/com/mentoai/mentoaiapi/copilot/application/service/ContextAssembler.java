package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.application.dto.*;
import com.mentoai.mentoaiapi.copilot.domain.entity.FonteCopiloto;
import com.mentoai.mentoaiapi.copilot.domain.enums.TipoFonteCopiloto;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class ContextAssembler {
    private final CopilotContextProperties limits;
    public ContextAssembler(CopilotContextProperties limits) { this.limits = limits; }

    public ContextoCopiloto montar(DadosContextoCopiloto dados) {
        var c = dados.cliente();
        var cliente = new ContextoCopiloto.ClienteContexto(c.getId(), c.getNome(), c.getSegmento(), c.getPorte(),
                cortar(c.getResumoContextual()));
        var reunioes = dados.reunioes().stream().distinct().map(r ->
                new ContextoCopiloto.ReuniaoContexto(r.id(), r.data(), r.analiseId(), cortar(r.resumo()), r.sentimento())).toList();
        var insights = dados.insights().stream().distinct().map(i ->
                new ContextoCopiloto.InsightContexto(i.id(), i.reuniaoId(), i.tipo(), cortar(i.descricao()), i.severidade())).toList();
        var sinais = dados.sinais().stream().distinct().map(s ->
                new ContextoCopiloto.SinalContexto(s.id(), s.reuniaoId(), s.tipo(), cortar(s.descricao()),
                        cortar(s.evidencia()), s.relevancia())).toList();
        var alertas = dados.alertas().stream().distinct().map(a ->
                new ContextoCopiloto.AlertaContexto(a.id(), a.reuniaoId(), a.sinalId(), a.prioridade(), cortar(a.motivo()))).toList();
        List<FonteCopiloto> fontes = new ArrayList<>();
        fontes.add(new FonteCopiloto(TipoFonteCopiloto.CLIENTE, c.getId(), c.getId(), null));
        for (var r : reunioes) fontes.add(new FonteCopiloto(TipoFonteCopiloto.REUNIAO, r.id(), c.getId(), r.id()));
        for (var i : insights) fontes.add(new FonteCopiloto(TipoFonteCopiloto.INSIGHT, i.id(), c.getId(), i.reuniaoId()));
        for (var s : sinais) fontes.add(new FonteCopiloto(TipoFonteCopiloto.SINAL_COMERCIAL, s.id(), c.getId(), s.reuniaoId()));
        for (var a : alertas) fontes.add(new FonteCopiloto(TipoFonteCopiloto.ALERTA, a.id(), c.getId(), a.reuniaoId()));
        return new ContextoCopiloto(cliente, reunioes, insights, sinais, alertas, fontes);
    }

    private String cortar(String s) {
        if (s == null || s.length() <= limits.getTamanhoCampo()) return s;
        return s.substring(0, limits.getTamanhoCampo()) + "…";
    }
}
