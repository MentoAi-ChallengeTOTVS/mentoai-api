package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import java.time.LocalDateTime;

public class AnaliseIA {
    private Long id;
    private Reuniao reuniao;
    private String resumoExecutivo;
    private SentimentoGeral sentimentoGeral;
    private StatusProcessamento statusProcessamento;
    private LocalDateTime criacao;
    private LocalDateTime iniciadoEm;
    private LocalDateTime finalizadoEm;
    private String mensagemErro;

    public AnaliseIA() {
        this.statusProcessamento = StatusProcessamento.PENDENTE;
    }

    public AnaliseIA(Long id, Reuniao reuniao, String resumoExecutivo, SentimentoGeral sentimentoGeral,
                     StatusProcessamento statusProcessamento, LocalDateTime criacao, LocalDateTime iniciadoEm,
                     LocalDateTime finalizadoEm, String mensagemErro) {
        this.id = id;
        this.reuniao = reuniao;
        this.resumoExecutivo = resumoExecutivo;
        this.sentimentoGeral = sentimentoGeral;
        this.statusProcessamento = statusProcessamento;
        this.criacao = criacao;
        this.iniciadoEm = iniciadoEm;
        this.finalizadoEm = finalizadoEm;
        this.mensagemErro = mensagemErro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Reuniao getReuniao() { return reuniao; }
    public void setReuniao(Reuniao reuniao) { this.reuniao = reuniao; }
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

    @Override
    public String toString() {
        return "AnaliseIA{" + "id=" + id + ", sentimentoGeral=" + sentimentoGeral
                + ", statusProcessamento=" + statusProcessamento + ", criacao=" + criacao + '}';
    }
}
