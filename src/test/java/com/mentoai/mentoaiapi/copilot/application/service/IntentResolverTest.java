package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntentResolverTest {
    private final IntentResolver resolver = new IntentResolver();

    @Test void reconheceIntencoesENormalizacao() {
        assertEquals(IntencaoCopiloto.CLIENT_RISK, resolver.resolver("Há RÍSCO de cancelamento?").principal());
        assertEquals(IntencaoCopiloto.CLIENT_OPPORTUNITY, resolver.resolver("Existe oportunidade de expansão?").principal());
        assertEquals(IntencaoCopiloto.COMPETITOR_ANALYSIS, resolver.resolver("Quais concorrentes foram citados?").principal());
        assertEquals(IntencaoCopiloto.OBJECTION_ANALYSIS, resolver.resolver("Quais objeções surgiram?").principal());
        assertEquals(IntencaoCopiloto.CLIENT_EVOLUTION, resolver.resolver("Como foi a evolução?").principal());
        assertEquals(IntencaoCopiloto.CLIENT_SUMMARY, resolver.resolver("Faça um resumo").principal());
        assertEquals(IntencaoCopiloto.GENERAL_CONTEXT, resolver.resolver("O que devo considerar?").principal());
    }

    @Test void compoeRiscoEConcorrenciaEIgnoraSubstring() {
        var resultado = resolver.resolver("Quais riscos a concorrência representa?");
        assertEquals(IntencaoCopiloto.COMPETITOR_ANALYSIS, resultado.principal());
        assertTrue(resultado.detectadas().contains(IntencaoCopiloto.CLIENT_RISK));
        assertEquals(IntencaoCopiloto.GENERAL_CONTEXT, resolver.resolver("A empresa é um microconcorrentezinho?").principal());
    }
}
