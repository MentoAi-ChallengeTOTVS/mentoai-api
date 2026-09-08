package com.mentoai.mentoaiapi.meeting.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteRepository;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ClientePersistenceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ClienteContextoTest {
    @Test
    void mapperPreservaMemoriaIntegralmente() {
        Cliente cliente = new Cliente();
        cliente.setResumoContextual(" \nHistórico " + "longo ".repeat(1000));
        var mapper = new ClientePersistenceMapper();
        assertEquals(cliente.getResumoContextual(), mapper.toDomain(mapper.toJpaEntity(cliente)).getResumoContextual());
        cliente.setResumoContextual(null);
        assertNull(mapper.toDomain(mapper.toJpaEntity(cliente)).getResumoContextual());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\r\n"})
    void escritaRecusaConteudoInvalido(String resumo) {
        ClienteRepository repository = mock(ClienteRepository.class);
        assertThrows(IllegalArgumentException.class, () -> new ClienteService(repository).atualizarResumoContextual(1L, resumo));
        verifyNoInteractions(repository);
    }
}
