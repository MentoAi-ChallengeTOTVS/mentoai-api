package com.mentoai.mentoaiapi.user.presentation.rest.response;

import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        PerfilUsuario perfil,
        Boolean ativo,
        LocalDateTime criacao,
        LocalDateTime atualizacao
) {
}
