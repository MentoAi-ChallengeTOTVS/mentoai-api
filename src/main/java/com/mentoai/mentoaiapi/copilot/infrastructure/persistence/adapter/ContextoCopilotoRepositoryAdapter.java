package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto.*;
import com.mentoai.mentoaiapi.copilot.application.repository.ContextoCopilotoRepository;
import com.mentoai.mentoaiapi.analysis.domain.enums.*;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ContextoCopilotoRepositoryAdapter implements ContextoCopilotoRepository {
    static final String REUNIOES_HQL = """
            select new com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto$ReuniaoContexto(
                r.id, r.dataReuniao, a.id, a.resumoExecutivo, a.sentimentoGeral)
            from ReuniaoJpaEntity r
            left join AnaliseIAJpaEntity a on a.reuniao = r and a.statusProcessamento = :status
            where r.cliente.id = :clienteId
            order by r.dataReuniao desc, r.id desc
            """;
    static final String INSIGHTS_HQL = """
            select new com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto$InsightContexto(
                i.id, a.reuniao.id, i.tipo, i.descricao, i.severidade)
            from InsightJpaEntity i join i.analise a join a.reuniao r
            where r.cliente.id = :clienteId and r.id in :reuniaoIds
              and a.statusProcessamento = :status and i.tipo in :tipos
            order by case i.severidade when :alta then 0 when :media then 1 else 2 end,
                     r.dataReuniao desc, i.id desc
            """;
    static final String SINAIS_HQL = """
            select new com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto$SinalContexto(
                s.id, a.reuniao.id, s.tipo, s.descricao, s.evidencia, s.relevancia)
            from SinalComercialJpaEntity s join s.analise a join a.reuniao r
            where r.cliente.id = :clienteId and r.id in :reuniaoIds
              and a.statusProcessamento = :status and s.tipo in :tipos
            order by case s.relevancia when :alta then 0 when :media then 1 else 2 end,
                     r.dataReuniao desc, s.id desc
            """;
    static final String ALERTAS_HQL = """
            select new com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto$AlertaContexto(
                a.id, a.sinalComercial.analise.reuniao.id, a.sinalComercial.id, a.prioridade, a.motivo)
            from AlertaJpaEntity a where a.sinalComercial.id in :ids
            order by a.criacao desc, a.id desc
            """;
    private final EntityManager em;

    public ContextoCopilotoRepositoryAdapter(EntityManager em) { this.em = em; }

    @Override
    public List<ReuniaoContexto> reunioes(Long clienteId, int limite) {
        return em.createQuery(REUNIOES_HQL, ReuniaoContexto.class)
                .setParameter("clienteId", clienteId)
                .setParameter("status", StatusProcessamento.PROCESSADA)
                .setMaxResults(limite).getResultList();
    }

    @Override
    public List<InsightContexto> insights(Long clienteId, List<Long> reuniaoIds, List<TipoInsight> tipos, int limite) {
        if (reuniaoIds.isEmpty()) return List.of();
        return em.createQuery(INSIGHTS_HQL, InsightContexto.class)
                .setParameter("clienteId", clienteId).setParameter("status", StatusProcessamento.PROCESSADA)
                .setParameter("alta", Severidade.ALTA).setParameter("media", Severidade.MEDIA)
                .setParameter("reuniaoIds", reuniaoIds).setParameter("tipos", tipos).setMaxResults(limite).getResultList();
    }

    @Override
    public List<SinalContexto> sinais(Long clienteId, List<Long> reuniaoIds, List<TipoSinalComercial> tipos, int limite) {
        if (reuniaoIds.isEmpty()) return List.of();
        return em.createQuery(SINAIS_HQL, SinalContexto.class)
                .setParameter("clienteId", clienteId).setParameter("status", StatusProcessamento.PROCESSADA)
                .setParameter("alta", RelevanciaSinal.ALTA).setParameter("media", RelevanciaSinal.MEDIA)
                .setParameter("reuniaoIds", reuniaoIds).setParameter("tipos", tipos).setMaxResults(limite).getResultList();
    }

    @Override
    public List<AlertaContexto> alertas(List<Long> sinalIds, int limite) {
        if (sinalIds.isEmpty()) return List.of();
        return em.createQuery(ALERTAS_HQL, AlertaContexto.class)
                .setParameter("ids", sinalIds).setMaxResults(limite).getResultList();
    }
}
