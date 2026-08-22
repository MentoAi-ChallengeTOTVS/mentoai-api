package com.mentoai.mentoaiapi.user.domain.repository;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> listar();
    boolean existePorEmail(String email);
}
