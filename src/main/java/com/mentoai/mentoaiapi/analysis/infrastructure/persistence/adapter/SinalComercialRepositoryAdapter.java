package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.domain.repository.SinalComercialRepository;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.SinalComercialPersistenceMapper;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository.SpringDataSinalComercialRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SinalComercialRepositoryAdapter implements SinalComercialRepository {

    private final SpringDataSinalComercialRepository repository;
    private final SinalComercialPersistenceMapper mapper;
    private final EntityManager entityManager;

    public SinalComercialRepositoryAdapter(
            SpringDataSinalComercialRepository repository,
            SinalComercialPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public SinalComercial salvar(SinalComercial sinal) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(sinal, reference(sinal))));
    }

    @Override
    public List<SinalComercial> salvarTodos(List<SinalComercial> sinais) {
        List<SinalComercialJpaEntity> entities = sinais.stream()
                .map(sinal -> mapper.toJpaEntity(sinal, reference(sinal)))
                .toList();
        return repository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<SinalComercial> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<SinalComercial> buscarPorAnaliseId(Long analiseId) {
        return repository.findByAnalise_Id(analiseId).stream().map(mapper::toDomain).toList();
    }

    private AnaliseIAJpaEntity reference(SinalComercial sinal) {
        return entityManager.getReference(AnaliseIAJpaEntity.class, sinal.getAnalise().getId());
    }
}
