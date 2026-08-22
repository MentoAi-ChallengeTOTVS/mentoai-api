package com.mentoai.mentoaiapi.meeting.presentation.rest.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ClienteResponse;
import org.springframework.stereotype.Component;

@Component
public class ClienteRestMapper {

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getSegmento(), cliente.getPorte(),
                cliente.getCriacao(), cliente.getStatus());
    }
}
