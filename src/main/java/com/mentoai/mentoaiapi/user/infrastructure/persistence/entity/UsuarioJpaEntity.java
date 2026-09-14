package com.mentoai.mentoaiapi.user.infrastructure.persistence.entity;

import com.mentoai.mentoaiapi.shared.persistence.converter.BooleanToIntegerConverter;
import com.mentoai.mentoaiapi.user.domain.enums.PerfilUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "SENHA", nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERFIL", nullable = false, length = 30)
    private PerfilUsuario perfil;

    @Convert(converter = BooleanToIntegerConverter.class)
    @Column(name = "ATIVO", nullable = false, columnDefinition = "NUMBER(1)")
    private Boolean ativo;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    @Column(name = "ATUALIZACAO", nullable = false)
    private LocalDateTime atualizacao;

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
}
