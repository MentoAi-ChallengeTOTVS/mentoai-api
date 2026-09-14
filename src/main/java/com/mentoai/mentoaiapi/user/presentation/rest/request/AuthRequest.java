package com.mentoai.mentoaiapi.user.presentation.rest.request;

public record AuthRequest(
        String email,
        String senha
) {}