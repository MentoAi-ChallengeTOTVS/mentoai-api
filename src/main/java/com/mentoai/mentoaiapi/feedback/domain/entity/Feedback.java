package com.mentoai.mentoaiapi.feedback.domain.entity;

import java.time.LocalDateTime;

public class Feedback {
    private Long id;
    private Integer nota; // 1 a 5
    private String comentario;
    private String usuarioEmail;
    private LocalDateTime dataCriacao;

    public Feedback(Long id, Integer nota, String comentario, String usuarioEmail, LocalDateTime dataCriacao) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.usuarioEmail = usuarioEmail;
        this.dataCriacao = dataCriacao;
    }

    // Getters
    public Long getId() { return id; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}