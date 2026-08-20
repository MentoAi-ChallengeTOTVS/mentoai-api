package com.mentoai.mentoaiapi.meeting.domain.entity;

import java.time.LocalDateTime;

public class Transcricao {
    private Long id;
    private String conteudo;
    private String nomeArquivo;
    private String formatoArquivo;
    private String idioma;
    private Reuniao reuniao;
    private LocalDateTime criacao;

    public Transcricao() {
    }

    public Transcricao(Long id, String conteudo, String nomeArquivo, String formatoArquivo, String idioma,
                      Reuniao reuniao, LocalDateTime criacao) {
        this.id = id;
        this.conteudo = conteudo;
        this.nomeArquivo = nomeArquivo;
        this.formatoArquivo = formatoArquivo;
        this.idioma = idioma;
        this.reuniao = reuniao;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getFormatoArquivo() { return formatoArquivo; }
    public void setFormatoArquivo(String formatoArquivo) { this.formatoArquivo = formatoArquivo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Reuniao getReuniao() { return reuniao; }
    public void setReuniao(Reuniao reuniao) { this.reuniao = reuniao; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "Transcricao{" + "id=" + id + ", nomeArquivo='" + nomeArquivo + '\'' + ", formatoArquivo='"
                + formatoArquivo + '\'' + ", idioma='" + idioma + '\'' + ", criacao=" + criacao + '}';
    }
}
