package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {
    @Query("select c.resumoContextual from ClienteJpaEntity c where c.id = :id")
    String buscarResumoContextual(@Param("id") Long id);

    @Modifying
    @Query("update ClienteJpaEntity c set c.resumoContextual = :resumo where c.id = :id")
    int atualizarResumoContextual(@Param("id") Long id, @Param("resumo") String resumo);
}
