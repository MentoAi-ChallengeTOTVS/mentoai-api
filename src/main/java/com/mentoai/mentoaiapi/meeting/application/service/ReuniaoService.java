package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReuniaoService {

    private final ReuniaoRepository reuniaoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReuniaoService(
            ReuniaoRepository reuniaoRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository) {
        this.reuniaoRepository = reuniaoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reuniao criar(LocalDateTime dataReuniao, Integer duracaoMinutos, Long clienteId, Long usuarioId) {
        Cliente cliente = cliente(clienteId);
        Usuario usuario = usuario(usuarioId);
        return reuniaoRepository.salvar(
                new Reuniao(
                null,
                dataReuniao,
                duracaoMinutos,
                cliente,
                usuario,
                LocalDateTime.now()
        ));
    }

    @Transactional(readOnly = true)
    public Reuniao buscarPorId(Long id) {
        return reuniaoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reunião não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<Reuniao> listar() {
        return reuniaoRepository.listar();
    }

    @Transactional(readOnly = true)
    public List<Reuniao> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existePorId(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado: " + clienteId);
        }
        return reuniaoRepository.buscarPorClienteId(clienteId);
    }

    @Transactional
    public Reuniao atualizar(
            Long id, LocalDateTime dataReuniao, Integer duracaoMinutos, Long clienteId, Long usuarioId) {
        Reuniao reuniao = buscarPorId(id);
        reuniao.setDataReuniao(dataReuniao);
        reuniao.setDuracaoMinutos(duracaoMinutos);
        reuniao.setCliente(cliente(clienteId));
        reuniao.setUsuario(usuario(usuarioId));
        return reuniaoRepository.salvar(reuniao);
    }

    private Cliente cliente(Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }

    private Usuario usuario(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }
}
