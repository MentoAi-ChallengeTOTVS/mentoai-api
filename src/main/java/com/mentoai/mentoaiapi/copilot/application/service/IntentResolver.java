package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import java.text.Normalizer;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class IntentResolver {
    private static final List<Regra> REGRAS = List.of(
            new Regra(IntencaoCopiloto.COMPETITOR_ANALYSIS, List.of("concorrente", "concorrentes", "concorrencia", "competidor")),
            new Regra(IntencaoCopiloto.OBJECTION_ANALYSIS, List.of("objecao", "objecoes", "resistencia", "barreira")),
            new Regra(IntencaoCopiloto.CLIENT_RISK, List.of("risco", "riscos", "churn", "cancelamento", "cancelar", "insatisfacao")),
            new Regra(IntencaoCopiloto.CLIENT_OPPORTUNITY, List.of("oportunidade", "oportunidades", "expansao", "upsell", "orcamento", "venda")),
            new Regra(IntencaoCopiloto.CLIENT_EVOLUTION, List.of("evolucao", "evoluiu", "mudou", "historico", "tendencia")),
            new Regra(IntencaoCopiloto.CLIENT_SUMMARY, List.of("resumo", "situacao geral", "visao geral", "panorama")));

    public Resultado resolver(String pergunta) {
        String normal = " " + Normalizer.normalize(pergunta.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").replaceAll("[^a-z0-9]+", " ").trim() + " ";
        EnumSet<IntencaoCopiloto> detectadas = EnumSet.noneOf(IntencaoCopiloto.class);
        IntencaoCopiloto principal = null;
        for (Regra regra : REGRAS) {
            if (regra.palavras().stream().anyMatch(p -> normal.contains(" " + p + " "))) {
                detectadas.add(regra.intencao());
                if (principal == null) principal = regra.intencao();
            }
        }
        if (principal == null) {
            principal = IntencaoCopiloto.GENERAL_CONTEXT;
            detectadas.add(principal);
        }
        return new Resultado(principal, detectadas);
    }

    private record Regra(IntencaoCopiloto intencao, List<String> palavras) {}
    public record Resultado(IntencaoCopiloto principal, EnumSet<IntencaoCopiloto> detectadas) {}
}
