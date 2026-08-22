package com.mentoai.mentoaiapi.analysis.domain.repository;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import java.util.Optional;

public interface AnaliseIARepository {

    AnaliseIA salvar(AnaliseIA analise);
    Optional<AnaliseIA> buscarPorId(Long id);
    Optional<AnaliseIA> buscarPorReuniaoId(Long reuniaoId);
}
