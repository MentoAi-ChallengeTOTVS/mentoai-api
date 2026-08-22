package com.mentoai.mentoaiapi.meeting.domain.repository;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> listar();
    boolean existePorId(Long id);
}
