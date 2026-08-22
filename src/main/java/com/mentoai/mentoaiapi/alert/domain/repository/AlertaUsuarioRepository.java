package com.mentoai.mentoaiapi.alert.domain.repository;

import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import java.util.List;
import java.util.Optional;

public interface AlertaUsuarioRepository {

    AlertaUsuario salvar(AlertaUsuario alertaUsuario);
    Optional<AlertaUsuario> buscarPorId(Long id);
    List<AlertaUsuario> buscarPorUsuarioId(Long usuarioId);
    List<AlertaUsuario> buscarPorUsuarioIdELido(Long usuarioId, boolean lido);
}
