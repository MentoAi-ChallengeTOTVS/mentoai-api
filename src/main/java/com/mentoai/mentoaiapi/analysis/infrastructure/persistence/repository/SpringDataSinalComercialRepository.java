package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSinalComercialRepository extends JpaRepository<SinalComercialJpaEntity, Long> {

    List<SinalComercialJpaEntity> findByAnalise_Id(Long analiseId);
}
