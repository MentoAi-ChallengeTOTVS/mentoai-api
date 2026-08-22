package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ANALISE_IA")
public class AnaliseIAJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REUNIAO_ID", nullable = false, unique = true)
    private ReuniaoJpaEntity reuniao;

    @Lob
    @Column(name = "RESUMO_EXECUTIVO", columnDefinition = "CLOB")
    private String resumoExecutivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "SENTIMENTO_GERAL", length = 20)
    private SentimentoGeral sentimentoGeral;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_PROCESSAMENTO", nullable = false, length = 20)
    private StatusProcessamento statusProcessamento;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    @Column(name = "INICIADO_EM")
    private LocalDateTime iniciadoEm;

    @Column(name = "FINALIZADO_EM")
    private LocalDateTime finalizadoEm;

    @Lob
    @Column(name = "MENSAGEM_ERRO", columnDefinition = "CLOB")
    private String mensagemErro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ReuniaoJpaEntity getReuniao() { return reuniao; }
    public void setReuniao(ReuniaoJpaEntity reuniao) { this.reuniao = reuniao; }
    public String getResumoExecutivo() { return resumoExecutivo; }
    public void setResumoExecutivo(String resumoExecutivo) { this.resumoExecutivo = resumoExecutivo; }
    public SentimentoGeral getSentimentoGeral() { return sentimentoGeral; }
    public void setSentimentoGeral(SentimentoGeral sentimentoGeral) { this.sentimentoGeral = sentimentoGeral; }
    public StatusProcessamento getStatusProcessamento() { return statusProcessamento; }
    public void setStatusProcessamento(StatusProcessamento statusProcessamento) { this.statusProcessamento = statusProcessamento; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public LocalDateTime getIniciadoEm() { return iniciadoEm; }
    public void setIniciadoEm(LocalDateTime iniciadoEm) { this.iniciadoEm = iniciadoEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
    public void setFinalizadoEm(LocalDateTime finalizadoEm) { this.finalizadoEm = finalizadoEm; }
    public String getMensagemErro() { return mensagemErro; }
    public void setMensagemErro(String mensagemErro) { this.mensagemErro = mensagemErro; }
}
