package com.mentoai.mentoaiapi.meeting.domain.entity;

import java.time.LocalDateTime;

public class Cliente {
    private Long id;
    private String nome;
    private String segmento;
    private String porte;
    private LocalDateTime criacao;
    private Boolean status;

    public Cliente() {
    }

    public Cliente(Long id, String nome, String segmento, String porte, LocalDateTime criacao, Boolean status) {
        this.id = id;
        this.nome = nome;
        this.segmento = segmento;
        this.porte = porte;
        this.criacao = criacao;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSegmento() { return segmento; }
    public void setSegmento(String segmento) { this.segmento = segmento; }
    public String getPorte() { return porte; }
    public void setPorte(String porte) { this.porte = porte; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nome='" + nome + '\'' + ", segmento='" + segmento + '\''
                + ", porte='" + porte + '\'' + ", criacao=" + criacao + ", status=" + status + '}';
    }
}
