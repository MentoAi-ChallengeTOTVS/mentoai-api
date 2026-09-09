package com.mentoai.mentoaiapi.analysis.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaItemResponse;
import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaResponse;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.analysis.domain.repository.AnaliseIARepository;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnaliseIAServiceTest {

    private final AnaliseIARepository analiseRepository = mock(AnaliseIARepository.class);
    private final ReuniaoRepository reuniaoRepository = mock(ReuniaoRepository.class);
    private final AnaliseIAService service = new AnaliseIAService(analiseRepository, reuniaoRepository);

    @Test
    void separaTodosOsStatusEOrdenaOsGrupos() {
        LocalDateTime base = LocalDateTime.of(2026, 9, 8, 19, 0);
        AnaliseIA processando = analise(12L, StatusProcessamento.PROCESSANDO,
                base.plusMinutes(13), null, null);
        AnaliseIA processadaAntiga = analise(8L, StatusProcessamento.PROCESSADA,
                base.plusMinutes(2), base.plusMinutes(40), null);
        AnaliseIA erroSemFinalizacao = analise(13L, StatusProcessamento.ERRO,
                base.plusMinutes(4), null, "Falha antiga");
        AnaliseIA pendente = analise(11L, StatusProcessamento.PENDENTE,
                base.plusMinutes(12), null, null);
        AnaliseIA erro = analise(10L, StatusProcessamento.ERRO,
                base.plusMinutes(5), base.plusMinutes(55), "Falha ao processar análise");
        AnaliseIA processadaRecente = analise(9L, StatusProcessamento.PROCESSADA,
                base.plusMinutes(3), base.plusMinutes(40), null);
        when(analiseRepository.listar()).thenReturn(List.of(
                processando, processadaAntiga, erroSemFinalizacao, pendente, erro, processadaRecente));

        AnaliseFilaResponse response = service.consultarFila();

        assertEquals(List.of(11L, 12L), ids(response.fila()));
        assertEquals(List.of(StatusProcessamento.PENDENTE, StatusProcessamento.PROCESSANDO),
                response.fila().stream().map(AnaliseFilaItemResponse::status).toList());
        assertEquals(List.of(10L, 9L, 8L, 13L), ids(response.finalizados()));
        assertEquals(List.of(
                        StatusProcessamento.ERRO,
                        StatusProcessamento.PROCESSADA,
                        StatusProcessamento.PROCESSADA,
                        StatusProcessamento.ERRO),
                response.finalizados().stream().map(AnaliseFilaItemResponse::status).toList());

        AnaliseFilaItemResponse erroResponse = response.finalizados().getFirst();
        assertEquals(110L, erroResponse.reuniaoId());
        assertEquals(210L, erroResponse.clienteId());
        assertEquals("Cliente 10", erroResponse.clienteNome());
        assertEquals(base.plusMinutes(5), erroResponse.criadoEm());
        assertEquals(base.plusMinutes(6), erroResponse.iniciadoEm());
        assertEquals("Falha ao processar análise", erroResponse.mensagemErro());
        assertNull(response.finalizados().get(1).mensagemErro());
        assertThrows(UnsupportedOperationException.class, () -> response.fila().clear());
        verify(analiseRepository).listar();
    }

    @Test
    void retornaListasVaziasQuandoNaoExistemAnalises() {
        when(analiseRepository.listar()).thenReturn(List.of());

        AnaliseFilaResponse response = service.consultarFila();

        assertEquals(List.of(), response.fila());
        assertEquals(List.of(), response.finalizados());
    }

    private AnaliseIA analise(
            Long id,
            StatusProcessamento status,
            LocalDateTime criacao,
            LocalDateTime finalizadoEm,
            String mensagemErro) {
        Cliente cliente = new Cliente(200L + id, "Cliente " + id, "Tecnologia", "MEDIO", criacao, true);
        Usuario usuario = new Usuario(
                300L + id,
                "Usuário " + id,
                "usuario" + id + "@mentoai.com",
                "senha",
                PerfilUsuario.EXECUTIVO_COMERCIAL,
                true,
                criacao,
                criacao);
        Reuniao reuniao = new Reuniao(100L + id, criacao, 30, cliente, usuario, criacao);
        LocalDateTime iniciadoEm = status == StatusProcessamento.PENDENTE ? null : criacao.plusMinutes(1);
        return new AnaliseIA(
                id, reuniao, null, null, status, criacao, iniciadoEm, finalizadoEm, mensagemErro);
    }

    private List<Long> ids(List<AnaliseFilaItemResponse> itens) {
        return itens.stream().map(AnaliseFilaItemResponse::analiseId).toList();
    }
}
