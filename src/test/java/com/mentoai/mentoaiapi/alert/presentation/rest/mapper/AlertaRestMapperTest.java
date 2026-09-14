package com.mentoai.mentoaiapi.alert.presentation.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaResponse;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AlertaRestMapperTest {

    @Test
    void incluiAnaliseEReuniaoDaOrigemDoSinal() {
        LocalDateTime agora = LocalDateTime.now();
        Reuniao reuniao = new Reuniao();
        reuniao.setId(30L);
        AnaliseIA analise = new AnaliseIA();
        analise.setId(20L);
        analise.setReuniao(reuniao);
        SinalComercial sinal = new SinalComercial(
                10L, analise, TipoSinalComercial.RISCO_CHURN, "Risco", "Evidência", RelevanciaSinal.ALTA, agora);
        Alerta alerta = new Alerta(1L, sinal, PrioridadeAlerta.ALTA, "Risco", agora);

        AlertaResponse response = new AlertaRestMapper().toResponse(alerta);

        assertEquals(10L, response.sinalComercialId());
        assertEquals(20L, response.analiseId());
        assertEquals(30L, response.reuniaoId());
    }
}
