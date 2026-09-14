package com.mentoai.mentoaiapi.alert.infrastructure.persistence.repository;

import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaUsuarioJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAlertaUsuarioRepository extends JpaRepository<AlertaUsuarioJpaEntity, Long> {

    List<AlertaUsuarioJpaEntity> findByUsuario_Id(Long usuarioId);
    List<AlertaUsuarioJpaEntity> findByUsuario_IdAndLido(Long usuarioId, Boolean lido);
}
