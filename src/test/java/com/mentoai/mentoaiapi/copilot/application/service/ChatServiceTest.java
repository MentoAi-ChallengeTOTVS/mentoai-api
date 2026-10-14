package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.repository.*;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {
    private final ChatRepository chats = mock(ChatRepository.class);
    private final UsuarioRepository usuarios = mock(UsuarioRepository.class);
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final ChatService service = new ChatService(chats, usuarios, clientes, mock(PerguntaChatRepository.class));

    @Test void usaUsuarioAutenticadoENaoRevelaChatAlheio() {
        var usuario = new Usuario(); usuario.setId(7L);
        when(usuarios.buscarPorEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(chats.buscarPorIdEUsuarioId(3L, 7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(3L, "a@b.com"));
        verify(chats).buscarPorIdEUsuarioId(3L, 7L);
    }

    @Test void criaComClienteExistente() {
        var usuario = new Usuario(); usuario.setId(7L);
        var cliente = new Cliente(); cliente.setId(15L);
        when(usuarios.buscarPorEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(clientes.buscarPorId(15L)).thenReturn(Optional.of(cliente));
        when(chats.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Chat criado = service.criar("  Horizonte ", 15L, "a@b.com");
        assertSame(usuario, criado.getUsuario());
        assertSame(cliente, criado.getCliente());
        assertEquals("Horizonte", criado.getTitulo());
    }
}
