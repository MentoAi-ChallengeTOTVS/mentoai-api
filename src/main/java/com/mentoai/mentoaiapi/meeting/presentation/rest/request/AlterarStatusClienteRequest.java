package com.mentoai.mentoaiapi.meeting.presentation.rest.request;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusClienteRequest(@NotNull Boolean status) {
}
