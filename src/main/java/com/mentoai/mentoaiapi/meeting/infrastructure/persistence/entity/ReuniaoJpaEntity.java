package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "REUNIAO")
public class ReuniaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private ClienteJpaEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private UsuarioJpaEntity usuario;

    @Column(name = "DATA_REUNIAO", nullable = false)
    private LocalDateTime dataReuniao;

    @Column(name = "DURACAO_MINUTOS", nullable = false)
    private Integer duracaoMinutos;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteJpaEntity getCliente() { return cliente; }
    public void setCliente(ClienteJpaEntity cliente) { this.cliente = cliente; }
    public UsuarioJpaEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioJpaEntity usuario) { this.usuario = usuario; }
    public LocalDateTime getDataReuniao() { return dataReuniao; }
    public void setDataReuniao(LocalDateTime dataReuniao) { this.dataReuniao = dataReuniao; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
}
