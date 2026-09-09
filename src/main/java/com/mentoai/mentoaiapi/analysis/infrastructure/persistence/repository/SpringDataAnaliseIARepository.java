package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataAnaliseIARepository extends JpaRepository<AnaliseIAJpaEntity, Long> {

    Optional<AnaliseIAJpaEntity> findByReuniao_Id(Long reuniaoId);

    @Query("""
            SELECT a
            FROM AnaliseIAJpaEntity a
            JOIN FETCH a.reuniao r
            JOIN FETCH r.cliente
            JOIN FETCH r.usuario
            ORDER BY a.criacao ASC, a.id ASC
            """)
    List<AnaliseIAJpaEntity> findAllComReuniaoClienteEUsuario();
}
