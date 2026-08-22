package com.mentoai.mentoaiapi.alert.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaRepository;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.mapper.AlertaPersistenceMapper;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.repository.SpringDataAlertaRepository;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AlertaRepositoryAdapter implements AlertaRepository {

    private final SpringDataAlertaRepository repository;
    private final AlertaPersistenceMapper mapper;
    private final EntityManager entityManager;

    public AlertaRepositoryAdapter(
            SpringDataAlertaRepository repository,
            AlertaPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Alerta salvar(Alerta alerta) {
        SinalComercialJpaEntity sinal = entityManager.getReference(
                SinalComercialJpaEntity.class, alerta.getSinalComercial().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(alerta, sinal)));
    }

    @Override
    public Optional<Alerta> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Alerta> buscarPorSinalComercialId(Long sinalComercialId) {
        return repository.findBySinalComercial_Id(sinalComercialId).map(mapper::toDomain);
    }
}
