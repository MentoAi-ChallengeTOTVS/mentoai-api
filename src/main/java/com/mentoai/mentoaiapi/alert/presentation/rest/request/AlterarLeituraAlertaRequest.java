package com.mentoai.mentoaiapi.alert.presentation.rest.request;

import jakarta.validation.constraints.NotNull;

public record AlterarLeituraAlertaRequest(@NotNull Boolean lido) {
}
