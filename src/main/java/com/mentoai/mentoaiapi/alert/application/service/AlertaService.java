package com.mentoai.mentoaiapi.alert.application.service;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.repository.AlertaRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Transactional
    public Alerta salvar(Alerta alerta) {
        return alertaRepository.salvar(alerta);
    }

    @Transactional(readOnly = true)
    public Alerta buscarPorId(Long id) {
        return alertaRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Alerta buscarPorSinalComercial(Long sinalComercialId) {
        return alertaRepository.buscarPorSinalComercialId(sinalComercialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alerta não encontrado para o sinal comercial: " + sinalComercialId));
    }
}
