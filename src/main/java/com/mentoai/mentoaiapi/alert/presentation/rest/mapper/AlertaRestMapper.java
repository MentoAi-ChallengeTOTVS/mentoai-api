package com.mentoai.mentoaiapi.alert.presentation.rest.mapper;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaResponse;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaUsuarioResponse;
import org.springframework.stereotype.Component;

@Component
public class AlertaRestMapper {

    public AlertaResponse toResponse(Alerta alerta) {
        if (alerta == null) {
            return null;
        }
        Long sinalId = alerta.getSinalComercial() != null ? alerta.getSinalComercial().getId() : null;
        return new AlertaResponse(
                alerta.getId(),
                sinalId,
                alerta.getPrioridade(),
                alerta.getMotivo(),
                alerta.getCriacao()
        );
    }

    public AlertaUsuarioResponse toUsuarioResponse(AlertaUsuario alertaUsuario) {
        if (alertaUsuario == null) {
            return null;
        }

        Long alertaId = alertaUsuario.getAlerta() != null ? alertaUsuario.getAlerta().getId() : null;
        Long usuarioId = alertaUsuario.getUsuario() != null ? alertaUsuario.getUsuario().getId() : null;

        return new AlertaUsuarioResponse(
                alertaUsuario.getId(),
                alertaId,
                usuarioId,
                alertaUsuario.isLido(),
                alertaUsuario.getLidoEm()
        );
    }
}