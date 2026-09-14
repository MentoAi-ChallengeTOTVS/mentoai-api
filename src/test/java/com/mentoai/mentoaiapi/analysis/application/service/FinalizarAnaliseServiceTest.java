package com.mentoai.mentoaiapi.analysis.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import org.junit.jupiter.api.Test;

class FinalizarAnaliseServiceTest {

    @Test
    void geraAlertasApenasParaSinaisCriticosComRelevanciaAlta() {
        assertTrue(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.RISCO_CHURN, RelevanciaSinal.ALTA)));
        assertTrue(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.CONCORRENCIA, RelevanciaSinal.ALTA)));
        assertTrue(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.OBJECAO, RelevanciaSinal.ALTA)));
    }

    @Test
    void naoGeraAlertaParaRelevanciaMenorOuTipoNaoCritico() {
        assertFalse(FinalizarAnaliseService.deveGerarAlerta(null));
        assertFalse(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.RISCO_CHURN, RelevanciaSinal.MEDIA)));
        assertFalse(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.OBJECAO, RelevanciaSinal.BAIXA)));
        assertFalse(FinalizarAnaliseService.deveGerarAlerta(
                sinal(TipoSinalComercial.OPORTUNIDADE, RelevanciaSinal.ALTA)));
    }

    private SinalComercial sinal(TipoSinalComercial tipo, RelevanciaSinal relevancia) {
        return new SinalComercial(null, null, tipo, "Descrição", "Evidência", relevancia, null);
    }
}
