package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReuniaoRepository extends JpaRepository<ReuniaoJpaEntity, Long> {

    List<ReuniaoJpaEntity> findByCliente_Id(Long clienteId);
}
