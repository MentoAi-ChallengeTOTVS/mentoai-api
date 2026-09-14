package com.mentoai.mentoaiapi.analysis.domain.repository;

import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import java.util.List;
import java.util.Optional;

public interface InsightRepository {

    Insight salvar(Insight insight);
    List<Insight> salvarTodos(List<Insight> insights);
    Optional<Insight> buscarPorId(Long id);
    List<Insight> buscarPorAnaliseId(Long analiseId);
}
