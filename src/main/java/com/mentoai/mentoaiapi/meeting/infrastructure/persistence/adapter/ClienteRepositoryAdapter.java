package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository.SpringDataClienteRepository;
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
    public List<Cliente> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }
}
