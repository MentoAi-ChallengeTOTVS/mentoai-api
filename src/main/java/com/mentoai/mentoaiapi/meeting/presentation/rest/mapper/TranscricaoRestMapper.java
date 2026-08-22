package com.mentoai.mentoaiapi.meeting.presentation.rest.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.TranscricaoResponse;
import org.springframework.stereotype.Component;

@Component
public class TranscricaoRestMapper {

    public TranscricaoResponse toResponse(Transcricao transcricao) {
        return new TranscricaoResponse(transcricao.getId(), transcricao.getConteudo(), transcricao.getNomeArquivo(),
                transcricao.getFormatoArquivo(), transcricao.getIdioma(), transcricao.getReuniao().getId(),
                transcricao.getCriacao());
    }
}
