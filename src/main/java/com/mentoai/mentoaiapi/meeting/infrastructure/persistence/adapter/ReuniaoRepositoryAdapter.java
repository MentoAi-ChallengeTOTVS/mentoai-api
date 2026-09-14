package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ReuniaoPersistenceMapper;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository.SpringDataReuniaoRepository;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ReuniaoRepositoryAdapter implements ReuniaoRepository {

    private final SpringDataReuniaoRepository repository;
    private final ReuniaoPersistenceMapper mapper;
    private final EntityManager entityManager;

    public ReuniaoRepositoryAdapter(
            SpringDataReuniaoRepository repository,
            ReuniaoPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Reuniao salvar(Reuniao reuniao) {
        ClienteJpaEntity cliente = entityManager.getReference(ClienteJpaEntity.class, reuniao.getCliente().getId());
        UsuarioJpaEntity usuario = entityManager.getReference(UsuarioJpaEntity.class, reuniao.getUsuario().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(reuniao, cliente, usuario)));
    }

    @Override
    public Optional<Reuniao> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Reuniao> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Reuniao> buscarPorClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId).stream().map(mapper::toDomain).toList();
    }
}
