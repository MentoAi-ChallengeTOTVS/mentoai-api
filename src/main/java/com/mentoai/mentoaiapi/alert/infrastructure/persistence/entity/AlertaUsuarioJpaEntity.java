package com.mentoai.mentoaiapi.alert.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.shared.persistence.converter.BooleanToIntegerConverter;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.entity.UsuarioJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "ALERTA_USUARIO")
public class AlertaUsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ALERTA_ID", nullable = false)
    private AlertaJpaEntity alerta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private UsuarioJpaEntity usuario;

    @Convert(converter = BooleanToIntegerConverter.class)
    @Column(name = "LIDO", nullable = false, columnDefinition = "NUMBER(1)")
    private Boolean lido;

    @Column(name = "LIDO_EM")
    private LocalDateTime lidoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AlertaJpaEntity getAlerta() { return alerta; }
    public void setAlerta(AlertaJpaEntity alerta) { this.alerta = alerta; }
    public UsuarioJpaEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioJpaEntity usuario) { this.usuario = usuario; }
    public Boolean getLido() { return lido; }
    public void setLido(Boolean lido) { this.lido = lido; }
    public LocalDateTime getLidoEm() { return lidoEm; }
    public void setLidoEm(LocalDateTime lidoEm) { this.lidoEm = lidoEm; }
}
