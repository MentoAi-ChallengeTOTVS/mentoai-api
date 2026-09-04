package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteFiltro;
import com.mentoai.mentoaiapi.meeting.domain.repository.Pagina;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente criar(String nome, String segmento, String porte) {
        return clienteRepository.salvar(new Cliente(null, nome, segmento, porte, LocalDateTime.now(), true));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Pagina<Cliente> listar(ClienteFiltro filtro,int pagina,int tamanho,String ordenarPor,String direcao) 
    {
        return clienteRepository.listar(filtro,pagina,tamanho,ordenarPor,direcao);
    }

    @Transactional
    public Cliente atualizar(Long id, String nome, String segmento, String porte) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(nome);
        cliente.setSegmento(segmento);
        cliente.setPorte(porte);
        return clienteRepository.salvar(cliente);
    }

    @Transactional
    public Cliente alterarStatus(Long id, boolean status) {
        Cliente cliente = buscarPorId(id);
        cliente.setStatus(status);
        return clienteRepository.salvar(cliente);
    }
}
