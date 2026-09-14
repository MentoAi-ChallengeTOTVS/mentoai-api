package com.mentoai.mentoaiapi.user.application.service;

import com.mentoai.mentoaiapi.shared.infrastructure.security.JwtProvider;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import com.mentoai.mentoaiapi.user.presentation.rest.request.AuthRequest;
import com.mentoai.mentoaiapi.user.presentation.rest.response.AuthResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthResponse autenticar(AuthRequest request) {
        Usuario usuario = usuarioRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String role = usuario.getPerfil() != null ? usuario.getPerfil().name() : "EXECUTIVO_COMERCIAL";
        String token = jwtProvider.gerarToken(usuario.getEmail(), role);

        return new AuthResponse(token, "Bearer", usuario.getEmail(), role);
    }
}