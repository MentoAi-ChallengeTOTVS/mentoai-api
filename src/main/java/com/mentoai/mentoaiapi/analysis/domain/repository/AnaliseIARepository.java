package com.mentoai.mentoaiapi.analysis.domain.repository;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.ResumoReuniaoRecente;
import java.util.List;
import java.util.Optional;

public interface AnaliseIARepository {

    AnaliseIA salvar(AnaliseIA analise);
    Optional<AnaliseIA> buscarPorId(Long id);
    Optional<AnaliseIA> buscarPorReuniaoId(Long reuniaoId);
    List<ResumoReuniaoRecente> buscarResumosRecentesPorCliente(Long clienteId);
}
