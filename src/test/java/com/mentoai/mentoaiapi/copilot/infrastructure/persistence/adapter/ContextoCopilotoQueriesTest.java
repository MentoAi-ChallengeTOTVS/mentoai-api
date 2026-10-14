package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.*;
import com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity.*;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.*;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.*;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto.*;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ContextoCopilotoQueriesTest {
    @Test void hqlDasProjecoesEhValidoSemAcessarBanco() {
        var configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect")
                .setProperty("hibernate.boot.allow_jdbc_metadata_access", "false")
                .setProperty("hibernate.hbm2ddl.auto", "none")
                .addAnnotatedClass(ClienteJpaEntity.class)
                .addAnnotatedClass(UsuarioJpaEntity.class)
                .addAnnotatedClass(ReuniaoJpaEntity.class)
                .addAnnotatedClass(TranscricaoJpaEntity.class)
                .addAnnotatedClass(AnaliseIAJpaEntity.class)
                .addAnnotatedClass(InsightJpaEntity.class)
                .addAnnotatedClass(SinalComercialJpaEntity.class)
                .addAnnotatedClass(AlertaJpaEntity.class)
                .addAnnotatedClass(AlertaUsuarioJpaEntity.class)
                .addAnnotatedClass(ChatJpaEntity.class)
                .addAnnotatedClass(PerguntaChatJpaEntity.class);
        try (var factory = configuration.buildSessionFactory(); var session = factory.openSession()) {
            assertDoesNotThrow(() -> session.createSelectionQuery(ContextoCopilotoRepositoryAdapter.REUNIOES_HQL, ReuniaoContexto.class));
            assertDoesNotThrow(() -> session.createSelectionQuery(ContextoCopilotoRepositoryAdapter.INSIGHTS_HQL, InsightContexto.class));
            assertDoesNotThrow(() -> session.createSelectionQuery(ContextoCopilotoRepositoryAdapter.SINAIS_HQL, SinalContexto.class));
            assertDoesNotThrow(() -> session.createSelectionQuery(ContextoCopilotoRepositoryAdapter.ALERTAS_HQL, AlertaContexto.class));
        }
    }
}
