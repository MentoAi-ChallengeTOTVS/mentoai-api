package com.mentoai.mentoaiapi.meeting.domain.repository;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Pagina<Cliente> listar(ClienteFiltro filtro,int pagina,int tamanho,String ordenarPor,String direcao);

    boolean existePorId(Long id);
}
