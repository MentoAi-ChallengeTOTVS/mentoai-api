package com.mentoai.mentoaiapi.meeting.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Long id;
    private String nome;
    private String segmento;
    private String porte;

    private List<Reuniao> reunioes;
    public Cliente() {
        this.reunioes = new ArrayList<>();
    }

    public Cliente(Long id, String nome, String segmento, String porte) {
        this.id = id;
        this.nome = nome;
        this.segmento = segmento;
        this.porte = porte;
        this.reunioes = new ArrayList<>();
    }

    public List<Reuniao> obterHistorico() {
        return new ArrayList<>();
    }

    public void adicionarReuniao(Reuniao reuniao) {
        reunioes.add(reuniao);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", segmento='" + segmento + '\'' +
                ", porte='" + porte + '\'' +
                '}';
    }
}