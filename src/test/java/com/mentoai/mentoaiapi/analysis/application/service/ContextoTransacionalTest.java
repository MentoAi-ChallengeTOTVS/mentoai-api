package com.mentoai.mentoaiapi.analysis.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mentoai.mentoaiapi.analysis.application.ai.ResumoContextualPrompt;
import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.analysis.domain.entity.*;
import com.mentoai.mentoaiapi.analysis.domain.enums.*;
import com.mentoai.mentoaiapi.analysis.domain.repository.*;
import com.mentoai.mentoaiapi.meeting.application.service.*;
import com.mentoai.mentoaiapi.meeting.domain.entity.*;
import com.mentoai.mentoaiapi.meeting.domain.repository.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

class ContextoTransacionalTest {
    private final AnaliseIARepository analises = mock(AnaliseIARepository.class);
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final InsightRepository insights = mock(InsightRepository.class);
    private final SinalComercialRepository sinais = mock(SinalComercialRepository.class);
    private final AiProvider provider = mock(AiProvider.class);
    private final List<Connection> connections = new ArrayList<>();
    private DataSourceTransactionManager tx;
    private ProcessarAnaliseService processador;
    private AnaliseIA analise;
    private boolean finalizacaoCommitada;
    private boolean escritaContextual;
    private boolean falharCommitContextual;
    private String memoria = "anterior";

    @BeforeEach
    void preparar() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenAnswer(invocation -> {
            Connection connection = mock(Connection.class);
            when(connection.getAutoCommit()).thenReturn(true);
            doAnswer(call -> {
                if (escritaContextual && falharCommitContextual) throw new SQLException("commit contextual falhou");
                return null;
            }).when(connection).commit();
            connections.add(connection);
            return connection;
        });
        tx = new DataSourceTransactionManager(dataSource);
        tx.setRollbackOnCommitFailure(true);
        Cliente cliente = new Cliente(1L, "Cliente", "Setor", "Porte", LocalDateTime.now(), true);
        Reuniao reuniao = new Reuniao(2L, LocalDateTime.now(), 30, cliente, null, LocalDateTime.now());
        analise = new AnaliseIA(3L, reuniao, null, null, StatusProcessamento.PENDENTE,
                LocalDateTime.now(), null, null, null);
        when(analises.buscarPorId(3L)).thenReturn(java.util.Optional.of(analise));
        when(analises.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(clientes.buscarResumoContextual(1L)).thenAnswer(invocation -> memoria);
        when(analises.buscarResumosRecentesPorCliente(1L)).thenReturn(List.of(
                new ResumoReuniaoRecente(reuniao.getDataReuniao(), "Resumo executivo")));
        when(sinais.salvarTodos(anyList())).thenAnswer(invocation -> {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() { finalizacaoCommitada = true; }
            });
            return invocation.getArgument(0);
        });
        when(clientes.atualizarResumoContextual(1L, "novo")).thenAnswer(invocation -> {
            assertTrue(finalizacaoCommitada);
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            escritaContextual = true;
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() { memoria = "novo"; }
            });
            return 1;
        });
        AnaliseIAService analiseService = proxy(new AnaliseIAService(analises, mock(ReuniaoRepository.class)));
        var finalizador = proxy(new FinalizarAnaliseService(analiseService,
                proxy(new InsightService(insights)), proxy(new SinalComercialService(sinais))));
        var contexto = proxy(new ConsolidarContextoClienteService(proxy(new ClienteService(clientes)),
                analiseService, provider, new ResumoContextualPrompt()));
        TranscricaoService transcricoes = mock(TranscricaoService.class);
        when(transcricoes.buscarPorReuniao(2L)).thenReturn(new Transcricao(4L,
                "Transcrição completa que não deve chegar à consolidação", "reuniao.txt", "txt", "pt", reuniao, LocalDateTime.now()));
        GerarAnaliseAiService gerador = mock(GerarAnaliseAiService.class);
        when(gerador.gerar(any())).thenReturn(new ResultadoAnaliseAi("Resumo executivo", SentimentoGeral.NEUTRO, List.of(), List.of()));
        processador = proxy(new ProcessarAnaliseService(analiseService, transcricoes, gerador, finalizador, contexto));
        when(provider.gerar(any())).thenAnswer(invocation -> {
            assertTrue(finalizacaoCommitada, "IA contextual deve observar commit da finalização");
            assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
            var request = invocation.getArgument(0, com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest.class);
            assertFalse(request.prompt().contains("Transcrição completa"));
            return new AiResponse("\"novo\"", "GEMINI", "modelo");
        });
    }

    @Test
    void consolidaDepoisDoCommitSemTransacaoRemota() {
        assertSame(analise, processador.processar(3L));
        assertEquals("novo", memoria);
        verificarAnalisePreservada();
    }

    @Test
    void falhaNoCommitContextualPreservaAnaliseEMemoria() throws Exception {
        falharCommitContextual = true;
        assertDoesNotThrow(() -> processador.processar(3L));
        assertEquals("anterior", memoria);
        verificarAnalisePreservada();
        verify(connections.getLast()).rollback();
    }

    @Test
    void falhaDaIaContextualPreservaAnalise() {
        doThrow(new IllegalStateException("falha remota")).when(provider).gerar(any());
        assertDoesNotThrow(() -> processador.processar(3L));
        assertEquals("anterior", memoria);
        verificarAnalisePreservada();
    }

    @Test
    void rollbackDaFinalizacaoNaoConsolida() throws Exception {
        doAnswer(invocation -> {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        // Simula a releitura do estado persistido após rollback, sem banco neste teste.
                        analise = new AnaliseIA(3L, analise.getReuniao(), null, null,
                                StatusProcessamento.PROCESSANDO, LocalDateTime.now(), LocalDateTime.now(), null, null);
                    }
                }
            });
            throw new IllegalStateException("falha ao persistir sinais");
        }).when(sinais).salvarTodos(anyList());
        when(analises.buscarPorId(3L)).thenAnswer(invocation -> java.util.Optional.of(analise));
        assertEquals(StatusProcessamento.ERRO, processador.processar(3L).getStatusProcessamento());
        assertFalse(finalizacaoCommitada);
        verifyNoInteractions(provider);
        verify(connections.get(1)).rollback();
    }

    @Test
    void rejeitaTransacaoExternaAntesDeIniciar() {
        assertThrows(IllegalTransactionStateException.class,
                () -> new TransactionTemplate(tx).execute(status -> processador.processar(3L)));
        verifyNoInteractions(provider, analises);
    }

    private void verificarAnalisePreservada() {
        assertTrue(finalizacaoCommitada);
        assertEquals(StatusProcessamento.PROCESSADA, analise.getStatusProcessamento());
        assertEquals("Resumo executivo", analise.getResumoExecutivo());
        assertNull(analise.getMensagemErro());
        verify(analises, times(2)).salvar(any());
        verify(insights, times(1)).salvarTodos(anyList());
        verify(sinais, times(1)).salvarTodos(anyList());
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(T target) {
        ProxyFactory factory = new ProxyFactory(target);
        factory.setProxyTargetClass(true);
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(tx);
        interceptor.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        factory.addAdvice(interceptor);
        return (T) factory.getProxy();
    }
}
