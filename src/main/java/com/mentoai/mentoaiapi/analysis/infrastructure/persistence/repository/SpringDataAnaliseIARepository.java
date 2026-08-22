package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAnaliseIARepository extends JpaRepository<AnaliseIAJpaEntity, Long> {

    Optional<AnaliseIAJpaEntity> findByReuniao_Id(Long reuniaoId);
}
