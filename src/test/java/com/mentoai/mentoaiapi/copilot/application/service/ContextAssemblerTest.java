package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.application.dto.*;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContextAssemblerTest {
    @Test void limitaCampoEGeraFonteDeterministica() {
        var limits = new CopilotContextProperties();
        limits.setTamanhoCampo(4);
        var cliente = new Cliente(); cliente.setId(15L); cliente.setNome("Horizonte");
        var sinal = new ContextoCopiloto.SinalContexto(52L, 18L, null, "Descrição longa", "Evidência longa", null);
        var dados = new DadosContextoCopiloto(cliente, List.of(), List.of(), List.of(sinal), List.of());
        var contexto = new ContextAssembler(limits).montar(dados);
        assertEquals("Desc…", contexto.sinais().getFirst().descricao());
        assertEquals(com.mentoai.mentoaiapi.copilot.domain.enums.TipoFonteCopiloto.CLIENTE, contexto.fontes().getFirst().tipo());
        assertEquals(52L, contexto.fontes().get(1).id());
        assertEquals(18L, contexto.fontes().get(1).reuniaoId());
    }
}
