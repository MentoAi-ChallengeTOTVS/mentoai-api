package com.mentoai.mentoaiapi.alert.domain.repository;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlertaRepository {

    Alerta salvar(Alerta alerta);
    Optional<Alerta> buscarPorId(Long id);
    Optional<Alerta> buscarPorSinalComercialId(Long sinalComercialId);
    Page<Alerta> listar(Pageable pageable);
}
