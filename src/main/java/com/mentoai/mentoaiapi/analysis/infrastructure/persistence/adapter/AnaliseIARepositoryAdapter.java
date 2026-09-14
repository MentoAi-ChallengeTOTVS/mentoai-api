package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.ResumoReuniaoRecente;
import com.mentoai.mentoaiapi.analysis.domain.repository.AnaliseIARepository;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.AnaliseIAPersistenceMapper;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository.SpringDataAnaliseIARepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

@Repository
public class AnaliseIARepositoryAdapter implements AnaliseIARepository {

    static final String RESUMOS_RECENTES_SQL = """
            SELECT r.DATA_REUNIAO AS dataReuniao, a.RESUMO_EXECUTIVO AS resumoExecutivo
            FROM ANALISE_IA a
            JOIN REUNIAO r ON r.ID = a.REUNIAO_ID
            WHERE r.CLIENTE_ID = :clienteId
              AND a.STATUS_PROCESSAMENTO = 'PROCESSADA'
              AND a.RESUMO_EXECUTIVO IS NOT NULL
              AND REGEXP_LIKE(a.RESUMO_EXECUTIVO, '[^[:space:]]')
            ORDER BY r.DATA_REUNIAO DESC, r.ID DESC
            FETCH FIRST 5 ROWS ONLY
            """;

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

    @Override
    public List<AnaliseIA> listar() {
        return repository.findAllComReuniaoClienteEUsuario().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ResumoReuniaoRecente> buscarResumosRecentesPorCliente(Long clienteId) {
        // Materializa apenas estes dois escalares; não instancia entidades nem relacionamentos.
        NativeQuery<Object[]> query = entityManager.createNativeQuery(RESUMOS_RECENTES_SQL)
                .unwrap(NativeQuery.class);
        query.addScalar("dataReuniao", LocalDateTime.class);
        query.addScalar("resumoExecutivo", StandardBasicTypes.MATERIALIZED_CLOB);
        query.setParameter("clienteId", clienteId);
        return query.getResultList().stream()
                .map(row -> new ResumoReuniaoRecente((LocalDateTime) row[0], (String) row[1]))
                .toList();
    }
}
