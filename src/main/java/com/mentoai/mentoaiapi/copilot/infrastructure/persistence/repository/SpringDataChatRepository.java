package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataChatRepository extends JpaRepository<ChatJpaEntity, Long> {

    List<ChatJpaEntity> findByUsuario_Id(Long usuarioId);
}
