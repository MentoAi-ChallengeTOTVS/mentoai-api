package com.mentoai.mentoaiapi.user.presentation.rest.response;

public record AuthResponse(
        String token,
        String tipo,
        String email,
        String role
) {}