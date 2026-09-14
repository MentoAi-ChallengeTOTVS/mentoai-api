package com.mentoai.mentoaiapi.user.domain.repository;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;


public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Page<Usuario> listar(Pageable pageable);
    boolean existePorEmail(String email);
}
