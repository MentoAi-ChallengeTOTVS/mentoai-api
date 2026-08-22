package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.repository.AnaliseIARepository;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.AnaliseIAPersistenceMapper;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository.SpringDataAnaliseIARepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AnaliseIARepositoryAdapter implements AnaliseIARepository {

    private final SpringDataAnaliseIARepository repository;
    private final AnaliseIAPersistenceMapper mapper;
    private final EntityManager entityManager;

    public AnaliseIARepositoryAdapter(
            SpringDataAnaliseIARepository repository,
            AnaliseIAPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public AnaliseIA salvar(AnaliseIA analise) {
        ReuniaoJpaEntity reuniao = entityManager.getReference(ReuniaoJpaEntity.class, analise.getReuniao().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(analise, reuniao)));
    }

    @Override
    public Optional<AnaliseIA> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<AnaliseIA> buscarPorReuniaoId(Long reuniaoId) {
        return repository.findByReuniao_Id(reuniaoId).map(mapper::toDomain);
    }
}
