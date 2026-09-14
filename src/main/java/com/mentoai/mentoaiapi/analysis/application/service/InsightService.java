package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.repository.InsightRepository;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InsightService {

    private final InsightRepository insightRepository;

    public InsightService(InsightRepository insightRepository) {
        this.insightRepository = insightRepository;
    }

    @Transactional
    public Insight salvar(Insight insight) {
        return insightRepository.salvar(insight);
    }

    @Transactional
    public List<Insight> salvarTodos(List<Insight> insights) {
        return insightRepository.salvarTodos(insights);
    }

    @Transactional(readOnly = true)
    public Insight buscarPorId(Long id) {
        return insightRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insight não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Insight> listarPorAnalise(Long analiseId) {
        return insightRepository.buscarPorAnaliseId(analiseId);
    }
}
