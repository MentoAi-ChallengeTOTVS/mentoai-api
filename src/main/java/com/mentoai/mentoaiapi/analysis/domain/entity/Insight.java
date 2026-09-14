package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import java.time.LocalDateTime;

public class Insight {
    private Long id;
    private AnaliseIA analise;
    private TipoInsight tipo;
    private String descricao;
    private Severidade severidade;
    private LocalDateTime criacao;

    public Insight() {
    }

    public Insight(Long id, AnaliseIA analise, TipoInsight tipo, String descricao, Severidade severidade,
                   LocalDateTime criacao) {
        this.id = id;
        this.analise = analise;
        this.tipo = tipo;
        this.descricao = descricao;
        this.severidade = severidade;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnaliseIA getAnalise() { return analise; }
    public void setAnalise(AnaliseIA analise) { this.analise = analise; }
    public TipoInsight getTipo() { return tipo; }
    public void setTipo(TipoInsight tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Severidade getSeveridade() { return severidade; }
    public void setSeveridade(Severidade severidade) { this.severidade = severidade; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "Insight{" + "id=" + id + ", tipo=" + tipo + ", descricao='" + descricao + '\''
                + ", severidade=" + severidade + ", criacao=" + criacao + '}';
    }
}
