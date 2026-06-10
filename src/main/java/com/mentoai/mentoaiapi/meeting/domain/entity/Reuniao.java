package com.mentoai.mentoaiapi.meeting.domain.entity;


import com.mentoai.mentoaiapi.meeting.domain.enums.StatusProcessamento;

import java.time.LocalDateTime;

public class Reuniao {

    private Long id;
    private LocalDateTime dataReuniao;
    private Integer duracaoMinutos;
    private StatusProcessamento statusProcessamento;
    private Cliente cliente;

    public Reuniao() {
    }

    public Reuniao(
            Long id,
            LocalDateTime dataReuniao,
            Integer duracaoMinutos,
            StatusProcessamento statusProcessamento,
            Cliente cliente
    ) {
        this.id = id;
        this.dataReuniao = dataReuniao;
        this.duracaoMinutos = duracaoMinutos;
        this.statusProcessamento = statusProcessamento;
        this.cliente = cliente;
    }

    public void iniciarAnalise() {
        this.statusProcessamento = StatusProcessamento.PROCESSANDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataReuniao() {
        return dataReuniao;
    }

    public void setDataReuniao(LocalDateTime dataReuniao) {
        this.dataReuniao = dataReuniao;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public StatusProcessamento getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Reuniao{" +
                "id=" + id +
                ", dataReuniao=" + dataReuniao +
                ", duracaoMinutos=" + duracaoMinutos +
                ", statusProcessamento=" + statusProcessamento +
                '}';
    }
}