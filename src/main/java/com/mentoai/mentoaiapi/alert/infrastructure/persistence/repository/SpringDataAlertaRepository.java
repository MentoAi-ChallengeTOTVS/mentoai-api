package com.mentoai.mentoaiapi.alert.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAlertaRepository extends JpaRepository<AlertaJpaEntity, Long> {

    Optional<AlertaJpaEntity> findBySinalComercial_Id(Long sinalComercialId);
}
