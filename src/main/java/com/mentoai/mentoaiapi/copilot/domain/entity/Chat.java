package com.mentoai.mentoaiapi.copilot.domain.entity;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    private Long id;
    private String titulo;
    private LocalDateTime criadoEm;
    private Usuario usuario;
    private List<PerguntaChat> perguntas;

    public Chat() {
        this.perguntas = new ArrayList<>();
    }

    public Chat(
            Long id,
            String titulo,
            LocalDateTime criadoEm,
            Usuario usuario
    ) {
        this.id = id;
        this.titulo = titulo;
        this.criadoEm = criadoEm;
        this.usuario = usuario;
        this.perguntas = new ArrayList<>();
    }

    public void adicionarPergunta(PerguntaChat pergunta) {
        this.perguntas.add(pergunta);
    }

    public List<PerguntaChat> obterHistorico() {
        return perguntas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<PerguntaChat> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<PerguntaChat> perguntas) {
        this.perguntas = perguntas;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", criadoEm=" + criadoEm +
                '}';
    }
}