package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.RelevanciaSinal;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoSinalComercial;
import java.time.LocalDateTime;

public class SinalComercial {
    private Long id;
    private AnaliseIA analise;
    private TipoSinalComercial tipo;
    private String descricao;
    private String evidencia;
    private RelevanciaSinal relevancia;
    private LocalDateTime criacao;

    public SinalComercial() {
    }

    public SinalComercial(Long id, AnaliseIA analise, TipoSinalComercial tipo, String descricao, String evidencia,
                         RelevanciaSinal relevancia, LocalDateTime criacao) {
        this.id = id;
        this.analise = analise;
        this.tipo = tipo;
        this.descricao = descricao;
        this.evidencia = evidencia;
        this.relevancia = relevancia;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnaliseIA getAnalise() { return analise; }
    public void setAnalise(AnaliseIA analise) { this.analise = analise; }
    public TipoSinalComercial getTipo() { return tipo; }
    public void setTipo(TipoSinalComercial tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }
    public RelevanciaSinal getRelevancia() { return relevancia; }
    public void setRelevancia(RelevanciaSinal relevancia) { this.relevancia = relevancia; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "SinalComercial{" + "id=" + id + ", tipo=" + tipo + ", descricao='" + descricao + '\''
                + ", relevancia=" + relevancia + ", criacao=" + criacao + '}';
    }
}
