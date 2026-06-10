package com.mentoai.mentoaiapi.meeting.domain.entity;

public class Transcricao {

    private Long id;
    private String conteudo;
    private String idioma;
    private Reuniao reuniao;

    public Transcricao() {
    }

    public Transcricao(
            Long id,
            String conteudo,
            String idioma,
            Reuniao reuniao
    ) {
        this.id = id;
        this.conteudo = conteudo;
        this.idioma = idioma;
        this.reuniao = reuniao;
    }

    public void processarTexto() {
        System.out.println("Processando transcrição...");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Reuniao getReuniao() {
        return reuniao;
    }

    public void setReuniao(Reuniao reuniao) {
        this.reuniao = reuniao;
    }

    @Override
    public String toString() {
        return "Transcricao{" +
                "id=" + id +
                ", idioma='" + idioma + '\'' +
                '}';
    }
}