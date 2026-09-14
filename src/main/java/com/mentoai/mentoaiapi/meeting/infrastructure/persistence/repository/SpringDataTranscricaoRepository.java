package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.TranscricaoJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTranscricaoRepository extends JpaRepository<TranscricaoJpaEntity, Long> {

    Optional<TranscricaoJpaEntity> findByReuniao_Id(Long reuniaoId);
}
