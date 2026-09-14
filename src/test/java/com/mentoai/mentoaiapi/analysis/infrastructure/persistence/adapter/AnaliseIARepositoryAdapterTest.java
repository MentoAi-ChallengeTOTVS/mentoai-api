package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper.AnaliseIAPersistenceMapper;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.repository.SpringDataAnaliseIARepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnaliseIARepositoryAdapterTest {

    @Test
    void listaUsandoConsultaComRelacionamentosEMapeiaParaDominio() {
        SpringDataAnaliseIARepository springDataRepository = mock(SpringDataAnaliseIARepository.class);
        AnaliseIAPersistenceMapper mapper = mock(AnaliseIAPersistenceMapper.class);
        AnaliseIAJpaEntity primeiraJpa = new AnaliseIAJpaEntity();
        AnaliseIAJpaEntity segundaJpa = new AnaliseIAJpaEntity();
        AnaliseIA primeira = mock(AnaliseIA.class);
        AnaliseIA segunda = mock(AnaliseIA.class);
        when(springDataRepository.findAllComReuniaoClienteEUsuario())
                .thenReturn(List.of(primeiraJpa, segundaJpa));
        when(mapper.toDomain(primeiraJpa)).thenReturn(primeira);
        when(mapper.toDomain(segundaJpa)).thenReturn(segunda);
        AnaliseIARepositoryAdapter adapter = new AnaliseIARepositoryAdapter(
                springDataRepository, mapper, mock(EntityManager.class));

        List<AnaliseIA> resultado = adapter.listar();

        assertEquals(List.of(primeira, segunda), resultado);
        verify(springDataRepository).findAllComReuniaoClienteEUsuario();
        verify(mapper).toDomain(primeiraJpa);
        verify(mapper).toDomain(segundaJpa);
    }
}
