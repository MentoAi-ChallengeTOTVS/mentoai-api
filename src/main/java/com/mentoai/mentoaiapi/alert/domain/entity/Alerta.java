package com.mentoai.mentoaiapi.alert.domain.entity;

import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.analysis.domain.entity.SinalComercial;
import java.time.LocalDateTime;

public class Alerta {
    private Long id;
    private SinalComercial sinalComercial;
    private PrioridadeAlerta prioridade;
    private String motivo;
    private LocalDateTime criacao;

    public Alerta() {
    }

    public Alerta(Long id, SinalComercial sinalComercial, PrioridadeAlerta prioridade, String motivo,
                  LocalDateTime criacao) {
        this.id = id;
        this.sinalComercial = sinalComercial;
        this.prioridade = prioridade;
        this.motivo = motivo;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SinalComercial getSinalComercial() { return sinalComercial; }
    public void setSinalComercial(SinalComercial sinalComercial) { this.sinalComercial = sinalComercial; }
    public PrioridadeAlerta getPrioridade() { return prioridade; }
    public void setPrioridade(PrioridadeAlerta prioridade) { this.prioridade = prioridade; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "Alerta{" + "id=" + id + ", prioridade=" + prioridade + ", motivo='" + motivo + '\''
                + ", criacao=" + criacao + '}';
    }
}
