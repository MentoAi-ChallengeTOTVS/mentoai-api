package com.mentoai.mentoaiapi.user.presentation.rest.request;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusUsuarioRequest(@NotNull Boolean ativo) {
}
