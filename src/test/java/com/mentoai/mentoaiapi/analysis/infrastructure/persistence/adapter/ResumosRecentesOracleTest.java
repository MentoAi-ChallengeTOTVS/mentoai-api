package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/** Executa o SQL real sobre fixtures CLOB em CTEs Oracle; não cria tabelas nem altera dados. */
@EnabledIfEnvironmentVariable(named = "ORACLE_TEST_URL", matches = ".+")
class ResumosRecentesOracleTest {
    private static final String FIXTURES = """
            WITH REUNIAO AS (
                SELECT LEVEL AS ID, CASE WHEN LEVEL = 15 THEN 2 ELSE 1 END AS CLIENTE_ID,
                       TIMESTAMP '2026-09-01 14:00:00' + NUMTODSINTERVAL(FLOOR(LEVEL / 2), 'DAY') AS DATA_REUNIAO
                FROM DUAL CONNECT BY LEVEL <= 15
            ), ANALISE_IA AS (
                SELECT r.ID AS REUNIAO_ID,
                       CASE r.ID WHEN 8 THEN 'PENDENTE' WHEN 9 THEN 'PROCESSANDO'
                            WHEN 10 THEN 'ERRO' ELSE 'PROCESSADA' END AS STATUS_PROCESSAMENTO,
                       CASE r.ID
                            WHEN 7 THEN TO_CLOB(RPAD('x', 3000, 'x')) || TO_CLOB(RPAD('y', 3000, 'y'))
                            WHEN 11 THEN TO_CLOB(NULL)
                            WHEN 12 THEN EMPTY_CLOB()
                            WHEN 13 THEN TO_CLOB('   ')
                            WHEN 14 THEN TO_CLOB(CHR(9) || CHR(10) || CHR(13))
                            ELSE TO_CLOB('resumo' || r.ID) END AS RESUMO_EXECUTIVO
                FROM REUNIAO r
            )
            """;

    @Test
    @SuppressWarnings("unchecked")
    void filtraAntesDeLimitarOrdenaDesempataEMaterializaClobCompleto() {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver")
                .setProperty("hibernate.connection.url", System.getenv("ORACLE_TEST_URL"))
                .setProperty("hibernate.connection.username", System.getenv("ORACLE_TEST_USERNAME"))
                .setProperty("hibernate.connection.password", System.getenv("ORACLE_TEST_PASSWORD"))
                .setProperty("hibernate.hbm2ddl.auto", "none");
        try (var factory = configuration.buildSessionFactory(); var session = factory.openSession()) {
            NativeQuery<Object[]> query = session.createNativeQuery(
                    FIXTURES + AnaliseIARepositoryAdapter.RESUMOS_RECENTES_SQL, Object[].class);
            query.addScalar("dataReuniao", LocalDateTime.class);
            query.addScalar("resumoExecutivo", StandardBasicTypes.MATERIALIZED_CLOB);
            query.setParameter("clienteId", 1L);
            List<Object[]> rows = query.getResultList();
            assertEquals(5, rows.size());
            assertEquals("x".repeat(3000) + "y".repeat(3000), rows.getFirst()[1]);
            assertEquals(List.of("resumo6", "resumo5", "resumo4", "resumo3"),
                    rows.subList(1, 5).stream().map(row -> row[1]).toList());
            assertEquals(rows.get(0)[0], rows.get(1)[0], "Empate de data deve usar ID DESC");
            query.setParameter("clienteId", 2L);
            assertEquals("resumo15", query.getSingleResult()[1]);
            query.setParameter("clienteId", 999L);
            assertTrue(query.getResultList().isEmpty());
        }
    }
}
