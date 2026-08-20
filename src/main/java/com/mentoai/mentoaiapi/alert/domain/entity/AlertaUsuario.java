package com.mentoai.mentoaiapi.alert.domain.entity;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import java.time.LocalDateTime;

public class AlertaUsuario {
    private Long id;
    private Alerta alerta;
    private Usuario usuario;
    private boolean lido;
    private LocalDateTime lidoEm;

    public AlertaUsuario() {
        this.lido = false;
    }

    public AlertaUsuario(Long id, Alerta alerta, Usuario usuario, boolean lido, LocalDateTime lidoEm) {
        this.id = id;
        this.alerta = alerta;
        this.usuario = usuario;
        this.lido = lido;
        this.lidoEm = lidoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Alerta getAlerta() { return alerta; }
    public void setAlerta(Alerta alerta) { this.alerta = alerta; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
    public LocalDateTime getLidoEm() { return lidoEm; }
    public void setLidoEm(LocalDateTime lidoEm) { this.lidoEm = lidoEm; }

    @Override
    public String toString() {
        return "AlertaUsuario{" + "id=" + id + ", lido=" + lido + ", lidoEm=" + lidoEm + '}';
    }
}
