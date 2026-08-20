package com.mentoai.mentoaiapi.copilot.domain.entity;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import java.time.LocalDateTime;

public class Chat {
    private Long id;
    private String titulo;
    private Usuario usuario;
    private LocalDateTime criacao;

    public Chat() {
    }

    public Chat(Long id, String titulo, Usuario usuario, LocalDateTime criacao) {
        this.id = id;
        this.titulo = titulo;
        this.usuario = usuario;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "Chat{" + "id=" + id + ", titulo='" + titulo + '\'' + ", criacao=" + criacao + '}';
    }
}
