package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.shared.persistence.converter.BooleanToIntegerConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "CLIENTE")
public class ClienteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;

    @Column(name = "SEGMENTO", nullable = false, length = 100)
    private String segmento;

    @Column(name = "PORTE", nullable = false, length = 50)
    private String porte;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    @Convert(converter = BooleanToIntegerConverter.class)
    @Column(name = "STATUS", nullable = false, columnDefinition = "NUMBER(1)")
    private Boolean status;

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
}
