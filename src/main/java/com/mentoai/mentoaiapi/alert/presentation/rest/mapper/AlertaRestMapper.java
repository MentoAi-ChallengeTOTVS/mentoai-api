package com.mentoai.mentoaiapi.alert.presentation.rest.mapper;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaResponse;
import org.springframework.stereotype.Component;

@Component
public class AlertaRestMapper {

    public AlertaResponse toResponse(Alerta alerta) {
        return new AlertaResponse(alerta.getId(), alerta.getSinalComercial().getId(), alerta.getPrioridade(),
                alerta.getMotivo(), alerta.getCriacao());
    }
}
