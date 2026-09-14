package com.mentoai.mentoaiapi.meeting.domain.repository;

import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import java.util.List;
import java.util.Optional;

public interface ReuniaoRepository {

    Reuniao salvar(Reuniao reuniao);
    Optional<Reuniao> buscarPorId(Long id);
    List<Reuniao> listar();
    List<Reuniao> buscarPorClienteId(Long clienteId);
}
