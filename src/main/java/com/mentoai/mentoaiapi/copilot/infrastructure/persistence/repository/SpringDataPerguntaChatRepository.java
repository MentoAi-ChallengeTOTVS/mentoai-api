package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.PerguntaChatJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPerguntaChatRepository extends JpaRepository<PerguntaChatJpaEntity, Long> {

    List<PerguntaChatJpaEntity> findByChat_Id(Long chatId);
}
