package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.analysis.application.port.ai.*;
import com.mentoai.mentoaiapi.copilot.application.ai.CopilotPrompt;
import com.mentoai.mentoaiapi.copilot.application.dto.*;
import com.mentoai.mentoaiapi.copilot.domain.entity.*;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CopilotServiceTest {
    private final ChatService chats = mock(ChatService.class);
    private final PerguntaChatService perguntas = mock(PerguntaChatService.class);
    private final ContextRetriever retriever = mock(ContextRetriever.class);
    private final AiProvider provider = mock(AiProvider.class);
    private final CopilotContextProperties limits = new CopilotContextProperties();
    private final CopilotService service = new CopilotService(chats, perguntas, new IntentResolver(),
            retriever, new ContextAssembler(limits), new CopilotPrompt(limits), provider, limits);

    @Test void persisteSomenteAposRespostaValida() {
        var cliente = new Cliente(); cliente.setId(15L); cliente.setNome("Horizonte");
        var chat = new Chat(3L, "Teste", new Usuario(), cliente, null);
        when(chats.buscarPorId(3L, "a@b.com")).thenReturn(chat);
        when(retriever.recuperar(eq(15L), any())).thenReturn(new DadosContextoCopiloto(cliente,
                List.of(), List.of(), List.of(), List.of()));
        when(perguntas.historicoRecente(3L, 6)).thenReturn(List.of());
        when(provider.gerar(any())).thenReturn(new AiResponse("{\"resposta\":\"Dados insuficientes.\"}", "GEMINI", "m"));
        service.perguntar(3L, "a@b.com", "Quais riscos?");
        verify(perguntas).salvarConcluida(eq(chat), eq("Quais riscos?"), eq("Dados insuficientes."), any(), any());
    }

    @Test void falhaProviderNaoPersiste() {
        var cliente = new Cliente(); cliente.setId(15L);
        when(chats.buscarPorId(3L, "a@b.com")).thenReturn(new Chat(3L, "Teste", new Usuario(), cliente, null));
        when(retriever.recuperar(eq(15L), any())).thenReturn(new DadosContextoCopiloto(cliente,
                List.of(), List.of(), List.of(), List.of()));
        when(perguntas.historicoRecente(3L, 6)).thenReturn(List.of());
        when(provider.gerar(any())).thenThrow(new RuntimeException("timeout"));
        assertThrows(RuntimeException.class, () -> service.perguntar(3L, "a@b.com", "Quais riscos?"));
        verify(perguntas, never()).salvarConcluida(any(), any(), any(), any(), any());
    }

    @Test void chatInexistenteNaoConsultaProvider() {
        when(chats.buscarPorId(3L, "a@b.com")).thenThrow(new ResourceNotFoundException("Chat não encontrado"));
        assertThrows(ResourceNotFoundException.class, () -> service.perguntar(3L, "a@b.com", "Quais riscos?"));
        verifyNoInteractions(provider, perguntas, retriever);
    }
}
