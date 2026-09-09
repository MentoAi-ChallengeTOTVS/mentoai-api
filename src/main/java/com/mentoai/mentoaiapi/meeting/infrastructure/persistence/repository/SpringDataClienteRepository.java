package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    @Query("""
        SELECT c
        FROM ClienteJpaEntity c
        WHERE (:nome IS NULL
               OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:segmento IS NULL
               OR LOWER(c.segmento) = LOWER(:segmento))
          AND (:porte IS NULL
               OR LOWER(c.porte) = LOWER(:porte))
          AND (:status IS NULL
               OR c.status = :status)
        """)
    Page<ClienteJpaEntity> listarComFiltros(@Param("nome") String nome, @Param("segmento") String segmento, @Param("porte") String porte, @Param("status") Boolean status, Pageable pageable);
}