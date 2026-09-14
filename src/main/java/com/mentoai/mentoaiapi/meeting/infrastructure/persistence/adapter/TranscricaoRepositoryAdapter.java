package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.meeting.domain.repository.TranscricaoRepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.TranscricaoPersistenceMapper;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository.SpringDataTranscricaoRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TranscricaoRepositoryAdapter implements TranscricaoRepository {

    private final SpringDataTranscricaoRepository repository;
    private final TranscricaoPersistenceMapper mapper;
    private final EntityManager entityManager;

    public TranscricaoRepositoryAdapter(
            SpringDataTranscricaoRepository repository,
            TranscricaoPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Transcricao salvar(Transcricao transcricao) {
        ReuniaoJpaEntity reuniao = entityManager.getReference(
                ReuniaoJpaEntity.class, transcricao.getReuniao().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(transcricao, reuniao)));
    }

    @Override
    public Optional<Transcricao> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Transcricao> buscarPorReuniaoId(Long reuniaoId) {
        return repository.findByReuniao_Id(reuniaoId).map(mapper::toDomain);
    }

    @Override
    public List<Transcricao> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
