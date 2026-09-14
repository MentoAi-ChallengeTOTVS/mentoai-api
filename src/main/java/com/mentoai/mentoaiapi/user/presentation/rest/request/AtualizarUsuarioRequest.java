package com.mentoai.mentoaiapi.user.presentation.rest.request;

import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarUsuarioRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotNull PerfilUsuario perfil
) {
}
