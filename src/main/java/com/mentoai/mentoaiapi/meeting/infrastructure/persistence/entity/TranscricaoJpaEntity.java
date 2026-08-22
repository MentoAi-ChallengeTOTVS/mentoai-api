package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "TRANSCRICAO")
public class TranscricaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REUNIAO_ID", nullable = false, unique = true)
    private ReuniaoJpaEntity reuniao;

    @Lob
    @Column(name = "CONTEUDO", nullable = false, columnDefinition = "CLOB")
    private String conteudo;

    @Column(name = "NOME_ARQUIVO", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "FORMATO_ARQUIVO", nullable = false, length = 50)
    private String formatoArquivo;

    @Column(name = "IDIOMA", nullable = false, length = 20)
    private String idioma;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ReuniaoJpaEntity getReuniao() { return reuniao; }
    public void setReuniao(ReuniaoJpaEntity reuniao) { this.reuniao = reuniao; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getFormatoArquivo() { return formatoArquivo; }
    public void setFormatoArquivo(String formatoArquivo) { this.formatoArquivo = formatoArquivo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
}
