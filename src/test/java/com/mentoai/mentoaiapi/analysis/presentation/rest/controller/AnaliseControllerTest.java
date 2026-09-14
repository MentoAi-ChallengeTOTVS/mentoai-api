package com.mentoai.mentoaiapi.analysis.presentation.rest.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaItemResponse;
import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaResponse;
import com.mentoai.mentoaiapi.analysis.application.service.AnaliseIAService;
import com.mentoai.mentoaiapi.analysis.application.service.InsightService;
import com.mentoai.mentoaiapi.analysis.application.service.SinalComercialService;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.analysis.presentation.rest.mapper.AnaliseIARestMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AnaliseControllerTest {

    private final AnaliseIAService analiseService = mock(AnaliseIAService.class);
    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        AnaliseController controller = new AnaliseController(
                analiseService,
                mock(InsightService.class),
                mock(SinalComercialService.class),
                mock(AnaliseIARestMapper.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void retornaFilaComContratoCompleto() throws Exception {
        LocalDateTime criadoEm = LocalDateTime.of(2026, 9, 8, 20, 12);
        AnaliseFilaItemResponse item = new AnaliseFilaItemResponse(
                11L, 6L, 4L, "Grupo Atlas", StatusProcessamento.PENDENTE,
                criadoEm, null, null, null);
        when(analiseService.consultarFila()).thenReturn(new AnaliseFilaResponse(List.of(item), List.of()));

        mockMvc.perform(get("/api/v1/analises/fila"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fila[0].analiseId").value(11))
                .andExpect(jsonPath("$.fila[0].reuniaoId").value(6))
                .andExpect(jsonPath("$.fila[0].clienteId").value(4))
                .andExpect(jsonPath("$.fila[0].clienteNome").value("Grupo Atlas"))
                .andExpect(jsonPath("$.fila[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.fila[0].criadoEm").value("2026-09-08T20:12:00"))
                .andExpect(jsonPath("$.fila[0].iniciadoEm").hasJsonPath())
                .andExpect(jsonPath("$.fila[0].iniciadoEm").value(nullValue()))
                .andExpect(jsonPath("$.fila[0].finalizadoEm").hasJsonPath())
                .andExpect(jsonPath("$.fila[0].finalizadoEm").value(nullValue()))
                .andExpect(jsonPath("$.fila[0].mensagemErro").hasJsonPath())
                .andExpect(jsonPath("$.fila[0].mensagemErro").value(nullValue()))
                .andExpect(jsonPath("$.finalizados").isEmpty());
    }

    @Test
    void retornaDuasListasVaziasComStatusOk() throws Exception {
        when(analiseService.consultarFila()).thenReturn(new AnaliseFilaResponse(List.of(), List.of()));

        mockMvc.perform(get("/api/v1/analises/fila"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fila").isArray())
                .andExpect(jsonPath("$.fila").isEmpty())
                .andExpect(jsonPath("$.finalizados").isArray())
                .andExpect(jsonPath("$.finalizados").isEmpty());
    }
}
