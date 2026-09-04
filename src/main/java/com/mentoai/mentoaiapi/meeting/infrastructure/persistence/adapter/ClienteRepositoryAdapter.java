package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository.SpringDataClienteRepository;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteFiltro;
import com.mentoai.mentoaiapi.meeting.domain.repository.Pagina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteRepository repository;
    private final ClientePersistenceMapper mapper;

    public ClienteRepositoryAdapter(SpringDataClienteRepository repository, ClientePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(cliente)));
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Pagina<Cliente> listar(ClienteFiltro filtro, int pagina, int tamanho, String ordenarPor,String direcao) 
    {
        Sort.Direction direction = "desc".equalsIgnoreCase(direcao)? Sort.Direction.DESC:Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pagina,tamanho,Sort.by(direction, ordenarPor));

        Page<ClienteJpaEntity> resultado =clienteJpaRepository.listarComFiltros(filtro.nome(),filtro.segmento(),filtro.porte(),filtro.status(),pageable);

        List<Cliente> clientes = resultado.getContent().stream().map(clientePersistenceMapper::toDomain).toList();

        return new Pagina<>(clientes,resultado.getNumber(),resultado.getSize(),resultado.getTotalElements(),resultado.getTotalPages());
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }
}
