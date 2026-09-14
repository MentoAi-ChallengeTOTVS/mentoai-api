package com.mentoai.mentoaiapi.meeting.domain.entity;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import java.time.LocalDateTime;

public class Reuniao {
    private Long id;
    private LocalDateTime dataReuniao;
    private Integer duracaoMinutos;
    private Cliente cliente;
    private Usuario usuario;
    private LocalDateTime criacao;

    public Reuniao() {
    }

    public Reuniao(Long id, LocalDateTime dataReuniao, Integer duracaoMinutos, Cliente cliente,
                   Usuario usuario, LocalDateTime criacao) {
        this.id = id;
        this.dataReuniao = dataReuniao;
        this.duracaoMinutos = duracaoMinutos;
        this.cliente = cliente;
        this.usuario = usuario;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataReuniao() { return dataReuniao; }
    public void setDataReuniao(LocalDateTime dataReuniao) { this.dataReuniao = dataReuniao; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "Reuniao{" + "id=" + id + ", dataReuniao=" + dataReuniao + ", duracaoMinutos="
                + duracaoMinutos + ", criacao=" + criacao + '}';
    }
}
