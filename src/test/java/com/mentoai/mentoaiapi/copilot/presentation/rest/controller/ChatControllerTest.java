package com.mentoai.mentoaiapi.copilot.presentation.rest.controller;

import com.mentoai.mentoaiapi.copilot.application.service.*;
import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import com.mentoai.mentoaiapi.copilot.presentation.rest.mapper.*;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.shared.web.GlobalExceptionHandler;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ChatControllerTest {
    private final ChatService chats = mock(ChatService.class);
    private final CopilotService copiloto = mock(CopilotService.class);
    private MockMvc mvc;

    @BeforeEach void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new ChatController(chats, copiloto,
                new ChatRestMapper(), new PerguntaChatRestMapper()))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test void criaChatComClienteEUsuarioAutenticado() throws Exception {
        var cliente = new Cliente(); cliente.setId(15L); cliente.setNome("Horizonte");
        var usuario = new Usuario(); usuario.setId(7L);
        when(chats.criar("Horizonte", 15L, "a@b.com"))
                .thenReturn(new Chat(3L, "Horizonte", usuario, cliente, LocalDateTime.of(2026, 10, 9, 20, 0)));
        mvc.perform(post("/api/v1/copilot/chats")
                .principal(new TestingAuthenticationToken("a@b.com", "x"))
                .contentType("application/json").content("{\"titulo\":\"Horizonte\",\"clienteId\":15}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.clienteId").value(15));
        verify(chats).criar("Horizonte", 15L, "a@b.com");
    }

    @Test void rejeitaClienteAusente() throws Exception {
        mvc.perform(post("/api/v1/copilot/chats")
                .principal(new TestingAuthenticationToken("a@b.com", "x"))
                .contentType("application/json").content("{\"titulo\":\"Horizonte\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(chats);
    }

    @Test void detalheListaHistoricoPaginadoDoProprietario() throws Exception {
        var cliente = new Cliente(); cliente.setId(15L); cliente.setNome("Horizonte");
        var chat = new Chat(3L, "Horizonte", new Usuario(), cliente, LocalDateTime.now());
        when(chats.buscarPorId(3L, "a@b.com")).thenReturn(chat);
        when(chats.historico(3L, "a@b.com", 1, 2)).thenReturn(new PageImpl<>(List.of(), PageRequest.of(1, 2), 0));
        mvc.perform(get("/api/v1/copilot/chats/3?page=1&size=2")
                .principal(new TestingAuthenticationToken("a@b.com", "x")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.chat.clienteId").value(15));
        verify(chats).historico(3L, "a@b.com", 1, 2);
    }

    @Test void mensagemCriadaRetornaIntentEFontes() throws Exception {
        var cliente = new Cliente(); cliente.setId(15L); cliente.setNome("Horizonte");
        var chat = new Chat(3L, "Horizonte", new Usuario(), cliente, LocalDateTime.now());
        when(copiloto.perguntar(3L, "a@b.com", "Quais riscos?"))
                .thenReturn(new PerguntaChat(9L, chat, "Quais riscos?", "Risco identificado",
                        IntencaoCopiloto.CLIENT_RISK, List.of(), LocalDateTime.now()));
        mvc.perform(post("/api/v1/copilot/chats/3/messages")
                .principal(new TestingAuthenticationToken("a@b.com", "x"))
                .contentType("application/json").content("{\"pergunta\":\"Quais riscos?\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.intencao").value("CLIENT_RISK"))
                .andExpect(jsonPath("$.cliente.id").value(15));
    }

    @Test void chatAlheioPareceInexistente() throws Exception {
        when(chats.buscarPorId(3L, "a@b.com")).thenThrow(new ResourceNotFoundException("Chat não encontrado"));
        mvc.perform(get("/api/v1/copilot/chats/3")
                .principal(new TestingAuthenticationToken("a@b.com", "x")))
                .andExpect(status().isNotFound());
    }
}
