package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.shared.exception.BusinessException;
import com.mentoai.mentoaiapi.shared.exception.ConflictException;
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

    public void iniciarProcessamento() {
        if (statusProcessamento != StatusProcessamento.PENDENTE) {
            throw new ConflictException("A análise só pode iniciar o processamento quando estiver PENDENTE");
        }

        LocalDateTime agora = LocalDateTime.now();
        this.statusProcessamento = StatusProcessamento.PROCESSANDO;
        this.iniciadoEm = agora;
        this.finalizadoEm = null;
        this.mensagemErro = null;
    }

    public void concluir(String resumoExecutivo, SentimentoGeral sentimentoGeral) {
        if (statusProcessamento != StatusProcessamento.PROCESSANDO) {
            throw new ConflictException("A análise só pode ser concluída quando estiver PROCESSANDO");
        }
        if (resumoExecutivo == null || resumoExecutivo.isBlank()) {
            throw new BusinessException("O resumo executivo é obrigatório");
        }
        if (sentimentoGeral == null) {
            throw new BusinessException("O sentimento geral é obrigatório");
        }

        LocalDateTime agora = LocalDateTime.now();
        this.resumoExecutivo = resumoExecutivo;
        this.sentimentoGeral = sentimentoGeral;
        this.finalizadoEm = agora;
        this.statusProcessamento = StatusProcessamento.PROCESSADA;
        this.mensagemErro = null;
    }

    public void falhar(String mensagemErro) {
        if (statusProcessamento != StatusProcessamento.PROCESSANDO) {
            throw new ConflictException("A análise só pode registrar falha quando estiver PROCESSANDO");
        }
        if (mensagemErro == null || mensagemErro.isBlank()) {
            throw new BusinessException("A mensagem de erro é obrigatória");
        }

        LocalDateTime agora = LocalDateTime.now();
        this.statusProcessamento = StatusProcessamento.ERRO;
        this.finalizadoEm = agora;
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
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public LocalDateTime getIniciadoEm() { return iniciadoEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
    public String getMensagemErro() { return mensagemErro; }

    @Override
    public String toString() {
        return "AnaliseIA{" + "id=" + id + ", sentimentoGeral=" + sentimentoGeral
                + ", statusProcessamento=" + statusProcessamento + ", criacao=" + criacao + '}';
    }
}
