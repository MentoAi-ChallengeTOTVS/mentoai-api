package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.ChatJpaEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataChatRepository extends JpaRepository<ChatJpaEntity, Long> {

    @EntityGraph(attributePaths = {"usuario", "cliente"})
    Optional<ChatJpaEntity> findByIdAndUsuario_Id(Long id, Long usuarioId);
    @EntityGraph(attributePaths = {"usuario", "cliente"})
    Page<ChatJpaEntity> findByUsuario_Id(Long usuarioId, Pageable pageable);
}
