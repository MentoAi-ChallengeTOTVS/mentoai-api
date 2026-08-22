package com.mentoai.mentoaiapi.meeting.presentation.rest.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ReuniaoResponse;
import org.springframework.stereotype.Component;

@Component
public class ReuniaoRestMapper {

    public ReuniaoResponse toResponse(Reuniao reuniao) {
        return new ReuniaoResponse(reuniao.getId(), reuniao.getDataReuniao(), reuniao.getDuracaoMinutos(),
                reuniao.getCliente().getId(), reuniao.getUsuario().getId(), reuniao.getCriacao());
    }
}
