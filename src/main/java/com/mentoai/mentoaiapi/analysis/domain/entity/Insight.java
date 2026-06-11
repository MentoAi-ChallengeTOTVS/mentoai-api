package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;

public class Insight {
    private Long id;

    private TipoInsight tipo;

    private String descricao;

    private Severidade severidade;

    private AnaliseIA analise;
    public Insight(Long id, TipoInsight tipo, String descricao, Severidade severidade, AnaliseIA analise) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.severidade = severidade;
        this.analise = analise;
    }

    public void classificarInsight() {

        if(tipo == TipoInsight.CHURN) {
            this.severidade = Severidade.ALTA;
        }
        if(tipo == TipoInsight.OPORTUNIDADE || tipo == TipoInsight.CONCORRENTE) {
            this.severidade = Severidade.MEDIA;
        }
        if(tipo == TipoInsight.SATISFACAO) {
            this.severidade = Severidade.BAIXA;
        }

    }

    @Override
    public String toString() {
        return "Insight{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", severidade=" + severidade +
                ", descricao='" + descricao + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoInsight getTipo() {
        return tipo;
    }

    public void setTipo(TipoInsight tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Severidade getSeveridade() {
        return severidade;
    }

    public void setSeveridade(Severidade severidade) {
        this.severidade = severidade;
    }

    public AnaliseIA getAnalise() {
        return analise;
    }

    public void setAnalise(AnaliseIA analise) {
        this.analise = analise;
    }
}
