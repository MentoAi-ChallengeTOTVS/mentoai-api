package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {
}
