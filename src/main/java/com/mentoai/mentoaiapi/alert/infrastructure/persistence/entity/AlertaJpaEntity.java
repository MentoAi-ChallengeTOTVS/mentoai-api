package com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.SinalComercialJpaEntity;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALERTA")
public class AlertaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SINAL_COMERCIAL_ID", nullable = false, unique = true)
    private SinalComercialJpaEntity sinalComercial;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORIDADE", nullable = false, length = 10)
    private PrioridadeAlerta prioridade;

    @Lob
    @Column(name = "MOTIVO", nullable = false, columnDefinition = "CLOB")
    private String motivo;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SinalComercialJpaEntity getSinalComercial() { return sinalComercial; }
    public void setSinalComercial(SinalComercialJpaEntity sinalComercial) { this.sinalComercial = sinalComercial; }
    public PrioridadeAlerta getPrioridade() { return prioridade; }
    public void setPrioridade(PrioridadeAlerta prioridade) { this.prioridade = prioridade; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
}
