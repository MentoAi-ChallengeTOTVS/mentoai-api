package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.PerguntaChatJpaEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPerguntaChatRepository extends JpaRepository<PerguntaChatJpaEntity, Long> {

    @EntityGraph(attributePaths = {"chat", "chat.usuario", "chat.cliente"})
    Page<PerguntaChatJpaEntity> findByChat_Id(Long chatId, Pageable pageable);
    @EntityGraph(attributePaths = {"chat", "chat.usuario", "chat.cliente"})
    List<PerguntaChatJpaEntity> findByChat_IdAndRespostaIsNotNull(Long chatId, Pageable pageable);
}
