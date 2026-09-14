package com.mentoai.mentoaiapi.alert.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaUsuarioRepository;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.AlertaJpaEntity;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.mapper.AlertaUsuarioPersistenceMapper;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.repository.SpringDataAlertaUsuarioRepository;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AlertaUsuarioRepositoryAdapter implements AlertaUsuarioRepository {

    private final SpringDataAlertaUsuarioRepository repository;
    private final AlertaUsuarioPersistenceMapper mapper;
    private final EntityManager entityManager;

    public AlertaUsuarioRepositoryAdapter(
            SpringDataAlertaUsuarioRepository repository,
            AlertaUsuarioPersistenceMapper mapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public AlertaUsuario salvar(AlertaUsuario alertaUsuario) {
        AlertaJpaEntity alerta = entityManager.getReference(
                AlertaJpaEntity.class, alertaUsuario.getAlerta().getId());
        UsuarioJpaEntity usuario = entityManager.getReference(
                UsuarioJpaEntity.class, alertaUsuario.getUsuario().getId());
        return mapper.toDomain(repository.save(mapper.toJpaEntity(alertaUsuario, alerta, usuario)));
    }

    @Override
    public Optional<AlertaUsuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AlertaUsuario> buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuario_Id(usuarioId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<AlertaUsuario> buscarPorUsuarioIdELido(Long usuarioId, boolean lido) {
        return repository.findByUsuario_IdAndLido(usuarioId, lido).stream().map(mapper::toDomain).toList();
    }
}
