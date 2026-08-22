package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.repository.InsightRepository;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.InsightJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.InsightPersistenceMapper;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository.SpringDataInsightRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InsightRepositoryAdapter implements InsightRepository {

    private final SpringDataInsightRepository repository;
    private final InsightPersistenceMapper mapper;
    private final EntityManager entityManager;

    public InsightRepositoryAdapter(
            SpringDataInsightRepository repository,
            InsightPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Insight salvar(Insight insight) {
        AnaliseIAJpaEntity analise = reference(insight);
        return mapper.toDomain(repository.save(mapper.toJpaEntity(insight, analise)));
    }

    @Override
    public List<Insight> salvarTodos(List<Insight> insights) {
        List<InsightJpaEntity> entities = insights.stream()
                .map(insight -> mapper.toJpaEntity(insight, reference(insight)))
                .toList();
        return repository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Insight> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Insight> buscarPorAnaliseId(Long analiseId) {
        return repository.findByAnalise_Id(analiseId).stream().map(mapper::toDomain).toList();
    }

    private AnaliseIAJpaEntity reference(Insight insight) {
        return entityManager.getReference(AnaliseIAJpaEntity.class, insight.getAnalise().getId());
    }
}
