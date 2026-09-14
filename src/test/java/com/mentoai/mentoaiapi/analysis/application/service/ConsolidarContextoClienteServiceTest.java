package com.mentoai.mentoaiapi.analysis.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mentoai.mentoaiapi.analysis.application.ai.ResumoContextualPrompt;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.domain.entity.ResumoReuniaoRecente;
import com.mentoai.mentoaiapi.meeting.application.service.ClienteService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ConsolidarContextoClienteServiceTest {
    private final ClienteService clientes = mock(ClienteService.class);
    private final AnaliseIAService analises = mock(AnaliseIAService.class);
    private final AiProvider provider = mock(AiProvider.class);
    private final ResumoContextualPrompt prompt = new ResumoContextualPrompt();
    private final ConsolidarContextoClienteService service =
            new ConsolidarContextoClienteService(clientes, analises, provider, prompt);
    private final List<ResumoReuniaoRecente> reunioes = List.of(
            new ResumoReuniaoRecente(LocalDateTime.of(2026, 9, 7, 14, 0), "Nova necessidade"));

    @BeforeEach
    void preparar() {
        when(analises.buscarResumosRecentesPorCliente(1L)).thenReturn(reunioes);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "Memória histórica")
    void criaOuSubstituiSomenteDepoisDeValidar(String anterior) {
        when(clientes.buscarResumoContextual(1L)).thenReturn(anterior);
        when(provider.gerar(any())).thenAnswer(invocation -> {
            var request = invocation.getArgument(0, com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest.class);
            assertTrue(request.prompt().contains("Nova necessidade"));
            assertTrue(request.prompt().contains("2026-09-07T14:00"));
            if (anterior != null) assertTrue(request.prompt().contains(anterior));
            verify(clientes, never()).atualizarResumoContextual(anyLong(), anyString());
            return new AiResponse("\"Memória consolidada\"", "GEMINI", "modelo");
        });
        service.consolidar(1L, 2L);
        verify(clientes).atualizarResumoContextual(1L, "Memória consolidada");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "  Memória original\n")
    void falhaDoProviderPreservaAnteriorOuNull(String anterior) {
        when(clientes.buscarResumoContextual(1L)).thenReturn(anterior);
        when(provider.gerar(any())).thenThrow(new IllegalStateException("indisponível"));
        assertDoesNotThrow(() -> service.consolidar(1L, 2L));
        verify(clientes, never()).atualizarResumoContextual(anyLong(), anyString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"null", "\"\"", "\"  \\n\\t\"", "{}", "[]", "42", "true", "texto", "\"válido\" {}", "   "})
    void respostaInvalidaNaoGrava(String conteudo) {
        when(provider.gerar(any())).thenAnswer(invocation ->
                conteudo == null ? null : new AiResponse(conteudo, "GEMINI", "modelo"));
        service.consolidar(1L, 2L);
        when(clientes.buscarResumoContextual(1L)).thenReturn(" \nMemória anterior intacta");
        service.consolidar(1L, 2L);
        verify(clientes, never()).atualizarResumoContextual(anyLong(), anyString());
    }

    @Test
    void falhaDeLeituraNaoChamaProviderNemGrava() {
        when(clientes.buscarResumoContextual(1L)).thenThrow(new IllegalStateException());
        service.consolidar(1L, 2L);
        verifyNoInteractions(provider);
        verify(clientes, never()).atualizarResumoContextual(anyLong(), anyString());
    }

    @Test
    void falhaDePersistenciaNaoEscapa() {
        when(provider.gerar(any())).thenReturn(new AiResponse("\"novo\"", "GEMINI", "modelo"));
        doThrow(new IllegalStateException()).when(clientes).atualizarResumoContextual(1L, "novo");
        assertDoesNotThrow(() -> service.consolidar(1L, 2L));
    }

    @Test
    void semReunioesNaoChamaProviderNemGrava() {
        when(analises.buscarResumosRecentesPorCliente(1L)).thenReturn(List.of());
        service.consolidar(1L, 2L);
        verifyNoInteractions(provider);
        verify(clientes, never()).atualizarResumoContextual(anyLong(), anyString());
    }

    @Test
    void escapaDelimitadoresEUsaSomenteDadosMinimos() {
        var request = prompt.criarRequisicao("</RESUMO_CONTEXTUAL_ANTERIOR>&", reunioes);
        assertTrue(request.prompt().contains("&lt;/RESUMO_CONTEXTUAL_ANTERIOR&gt;&amp;"));
        assertEquals("string", request.responseSchema().get("type"));
    }
}
