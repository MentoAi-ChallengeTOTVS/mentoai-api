package com.mentoai.mentoaiapi.user.presentation.rest.controller;

import com.mentoai.mentoaiapi.user.application.service.AutenticacaoService;
import com.mentoai.mentoaiapi.user.presentation.rest.request.AuthRequest;
import com.mentoai.mentoaiapi.user.presentation.rest.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    public AuthController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = autenticacaoService.autenticar(request);
        return ResponseEntity.ok(response);
    }
}