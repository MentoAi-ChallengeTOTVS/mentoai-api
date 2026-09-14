package com.mentoai.mentoaiapi.user.presentation.rest.mapper;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.presentation.rest.response.UsuarioResponse;
import org.springframework.stereotype.Component;

@Component
public class UsuarioRestMapper {

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil(),
                usuario.getAtivo(), usuario.getCriacao(), usuario.getAtualizacao());
    }
}
