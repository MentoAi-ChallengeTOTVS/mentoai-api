package com.mentoai.mentoaiapi.alert.presentation.rest.mapper;

import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaUsuarioResponse;
import org.springframework.stereotype.Component;

@Component
public class AlertaUsuarioRestMapper {

    public AlertaUsuarioResponse toResponse(AlertaUsuario alertaUsuario) {
        return new AlertaUsuarioResponse(alertaUsuario.getId(), alertaUsuario.getAlerta().getId(),
                alertaUsuario.getUsuario().getId(), alertaUsuario.isLido(), alertaUsuario.getLidoEm());
    }
}
