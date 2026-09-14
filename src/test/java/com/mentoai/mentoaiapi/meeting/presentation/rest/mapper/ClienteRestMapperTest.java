package com.mentoai.mentoaiapi.meeting.presentation.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ClienteRestMapperTest {

    private final ClienteRestMapper mapper = new ClienteRestMapper();

    @Test
    void deveExporResumoContextual() {
        Cliente cliente = clienteComResumo("Resumo consolidado\ncom quebra de linha.");

        var response = mapper.toResponse(cliente);

        assertEquals(cliente.getResumoContextual(), response.resumoContextual());
    }

    @Test
    void devePreservarResumoContextualNulo() {
        Cliente cliente = clienteComResumo(null);

        var response = mapper.toResponse(cliente);

        assertNull(response.resumoContextual());
    }

    private Cliente clienteComResumo(String resumoContextual) {
        Cliente cliente = new Cliente(1L, "Cliente", "Tecnologia", "MEDIO",
                LocalDateTime.of(2026, 9, 2, 19, 47), true);
        cliente.setResumoContextual(resumoContextual);
        return cliente;
    }
}
