package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto;
import com.mentoai.mentoaiapi.copilot.application.repository.ContextoCopilotoRepository;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContextRetrieverTest {
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final ContextoCopilotoRepository repository = mock(ContextoCopilotoRepository.class);
    private final ContextRetriever retriever = new ContextRetriever(clientes, repository, new CopilotContextProperties());

    @Test void limitaContextoEConsultaSomenteReunioesSelecionadas() {
        var cliente = new Cliente(); cliente.setId(15L);
        when(clientes.buscarPorId(15L)).thenReturn(Optional.of(cliente));
        var reuniao = new ContextoCopiloto.ReuniaoContexto(18L, LocalDateTime.now(), 2L, "Resumo", null);
        when(repository.reunioes(15L, 5)).thenReturn(List.of(reuniao));
        retriever.recuperar(15L, EnumSet.of(IntencaoCopiloto.CLIENT_RISK));
        verify(repository).sinais(eq(15L), eq(List.of(18L)), anyList(), eq(8));
        verify(repository).insights(eq(15L), eq(List.of(18L)), anyList(), eq(5));
        verify(repository).alertas(anyList(), eq(5));
    }

    @Test void clienteSemReuniaoProduzContextoVazioELimitaGeral() {
        var cliente = new Cliente(); cliente.setId(15L);
        when(clientes.buscarPorId(15L)).thenReturn(Optional.of(cliente));
        var dados = retriever.recuperar(15L, EnumSet.of(IntencaoCopiloto.GENERAL_CONTEXT));
        assertTrue(dados.reunioes().isEmpty());
        verify(repository).reunioes(15L, 3);
        verify(repository).sinais(eq(15L), eq(List.of()), anyList(), eq(4));
        verify(repository, never()).alertas(anyList(), anyInt());
    }
}
