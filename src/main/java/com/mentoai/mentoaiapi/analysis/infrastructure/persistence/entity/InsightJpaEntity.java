package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "INSIGHT")
public class InsightJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ANALISE_ID", nullable = false)
    private AnaliseIAJpaEntity analise;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false, length = 30)
    private TipoInsight tipo;

    @Lob
    @Column(name = "DESCRICAO", nullable = false, columnDefinition = "CLOB")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEVERIDADE", nullable = false, length = 10)
    private Severidade severidade;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnaliseIAJpaEntity getAnalise() { return analise; }
    public void setAnalise(AnaliseIAJpaEntity analise) { this.analise = analise; }
    public TipoInsight getTipo() { return tipo; }
    public void setTipo(TipoInsight tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Severidade getSeveridade() { return severidade; }
    public void setSeveridade(Severidade severidade) { this.severidade = severidade; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
}
