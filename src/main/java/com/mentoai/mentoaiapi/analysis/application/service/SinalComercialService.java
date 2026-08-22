package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import com.mentoai.mentoaiapi.analysis.domain.repository.SinalComercialRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SinalComercialService {

    private final SinalComercialRepository sinalRepository;

    public SinalComercialService(SinalComercialRepository sinalRepository) {
        this.sinalRepository = sinalRepository;
    }

    @Transactional
    public SinalComercial salvar(SinalComercial sinal) {
        return sinalRepository.salvar(sinal);
    }

    @Transactional
    public List<SinalComercial> salvarTodos(List<SinalComercial> sinais) {
        return sinalRepository.salvarTodos(sinais);
    }

    @Transactional(readOnly = true)
    public SinalComercial buscarPorId(Long id) {
        return sinalRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sinal comercial não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<SinalComercial> listarPorAnalise(Long analiseId) {
        return sinalRepository.buscarPorAnaliseId(analiseId);
    }
}
