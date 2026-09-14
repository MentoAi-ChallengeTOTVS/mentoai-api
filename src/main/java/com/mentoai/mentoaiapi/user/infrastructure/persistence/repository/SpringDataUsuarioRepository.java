package com.mentoai.mentoaiapi.user.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    Optional<UsuarioJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
