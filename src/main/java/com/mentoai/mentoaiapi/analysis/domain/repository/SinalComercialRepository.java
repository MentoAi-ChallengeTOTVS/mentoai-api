package com.mentoai.mentoaiapi.analysis.domain.repository;

import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import java.util.List;
import java.util.Optional;

public interface SinalComercialRepository {

    SinalComercial salvar(SinalComercial sinalComercial);
    List<SinalComercial> salvarTodos(List<SinalComercial> sinaisComerciais);
    Optional<SinalComercial> buscarPorId(Long id);
    List<SinalComercial> buscarPorAnaliseId(Long analiseId);
}
