package com.mentoai.mentoaiapi.meeting.domain.repository;

import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import java.util.Optional;

public interface TranscricaoRepository {

    Transcricao salvar(Transcricao transcricao);
    Optional<Transcricao> buscarPorId(Long id);
    Optional<Transcricao> buscarPorReuniaoId(Long reuniaoId);
}
