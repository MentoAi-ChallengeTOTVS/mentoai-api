package com.mentoai.mentoaiapi.alert.application.service;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaRepository;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaUsuarioRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaUsuarioService {

    private final AlertaUsuarioRepository alertaUsuarioRepository;
    private final AlertaRepository alertaRepository;
    private final UsuarioRepository usuarioRepository;

    public AlertaUsuarioService(
            AlertaUsuarioRepository alertaUsuarioRepository,
            AlertaRepository alertaRepository,
            UsuarioRepository usuarioRepository) {
        this.alertaUsuarioRepository = alertaUsuarioRepository;
        this.alertaRepository = alertaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AlertaUsuario registrar(Long alertaId, Long usuarioId) {
        Alerta alerta = alertaRepository.buscarPorId(alertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + alertaId));
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + usuarioId));
        return alertaUsuarioRepository.salvar(new AlertaUsuario(null, alerta, usuario, false, null));
    }

    @Transactional(readOnly = true)
    public AlertaUsuario buscarPorId(Long id) {
        return alertaUsuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta de usuário não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<AlertaUsuario> listarPorUsuario(Long usuarioId) {
        return alertaUsuarioRepository.buscarPorUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<AlertaUsuario> listarPorUsuarioEStatusDeLeitura(Long usuarioId, boolean lido) {
        return alertaUsuarioRepository.buscarPorUsuarioIdELido(usuarioId, lido);
    }

    @Transactional
    public AlertaUsuario marcarComoLido(Long id) {
        AlertaUsuario alertaUsuario = buscarPorId(id);
        alertaUsuario.setLido(true);
        alertaUsuario.setLidoEm(LocalDateTime.now());
        return alertaUsuarioRepository.salvar(alertaUsuario);
    }
}
