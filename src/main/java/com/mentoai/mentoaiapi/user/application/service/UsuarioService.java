package com.mentoai.mentoaiapi.user.application.service;

import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario criar(String nome, String email, String senha, PerfilUsuario perfil) {
        if (usuarioRepository.existePorEmail(email)) {
            throw new ConflictException("Já existe usuário com o email informado");
        }
        LocalDateTime agora = LocalDateTime.now();
        return usuarioRepository.salvar(new Usuario(null, nome, email, senha, perfil, true, agora, agora));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.listar();
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
        usuario.setSenha(senha);
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
