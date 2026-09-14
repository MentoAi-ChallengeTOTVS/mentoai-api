package com.mentoai.mentoaiapi.user.domain.entity;

import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import java.time.LocalDateTime;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;
    private Boolean ativo;
    private LocalDateTime criacao;
    private LocalDateTime atualizacao;

    public Usuario() {
        this.ativo = true;
    }

    public Usuario(Long id, String nome, String email, String senha, PerfilUsuario perfil, Boolean ativo,
                   LocalDateTime criacao, LocalDateTime atualizacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = ativo;
        this.criacao = criacao;
        this.atualizacao = atualizacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public LocalDateTime getAtualizacao() { return atualizacao; }
    public void setAtualizacao(LocalDateTime atualizacao) { this.atualizacao = atualizacao; }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", email='" + email + '\''
                + ", perfil=" + perfil + ", ativo=" + ativo + ", criacao=" + criacao
                + ", atualizacao=" + atualizacao + '}';
    }
}
