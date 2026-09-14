package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.InsightJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInsightRepository extends JpaRepository<InsightJpaEntity, Long> {

    List<InsightJpaEntity> findByAnalise_Id(Long analiseId);
}
