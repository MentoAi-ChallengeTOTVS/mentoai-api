package com.mentoai.mentoaiapi.user.application.service;

import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario criar(String nome, String email, String senha, PerfilUsuario perfil) {
        if (usuarioRepository.existePorEmail(email)) {
            throw new ConflictException("Já existe usuário com o email informado");
        }

        String senhaCriptografada = passwordEncoder.encode(senha);
        LocalDateTime agora = LocalDateTime.now();

        return usuarioRepository.salvar(new Usuario(null, nome, email, senhaCriptografada, perfil, true, agora, agora));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.listar(pageable);
    }

    @Transactional
    public Usuario atualizar(Long id, String nome, String email, String senha, PerfilUsuario perfil) {
        Usuario usuario = buscarPorId(id);

        usuarioRepository.buscarPorEmail(email)
                .filter(encontrado -> !encontrado.getId().equals(id))
                .ifPresent(encontrado -> {
                    throw new ConflictException("Já existe usuário com o email informado");
                });

        usuario.setNome(nome);
        usuario.setEmail(email);

        // Atualiza a senha apenas se for enviada uma nova senha válida
        if (senha != null && !senha.isBlank()) {
            usuario.setSenha(passwordEncoder.encode(senha));
        }

        usuario.setPerfil(perfil);
        usuario.setAtualizacao(LocalDateTime.now());

        return usuarioRepository.salvar(usuario);
    }

    @Transactional
    public Usuario alterarStatus(Long id, boolean ativo) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(ativo);
        usuario.setAtualizacao(LocalDateTime.now());
        return usuarioRepository.salvar(usuario);
    }
}