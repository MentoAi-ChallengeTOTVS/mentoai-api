package com.mentoai.mentoaiapi.user.domain.entity;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;

public class DiretorComercial extends Usuario {

    private String setor;

    public DiretorComercial() {
    }

    public DiretorComercial(
            Long id,
            String nome,
            String email,
            String senha,
            String setor
    ) {
        super(id, nome, email, senha);
        this.setor = setor;
    }

    public void gerarRelatorio() {
        System.out.println("Gerando relatório estratégico...");
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @Override
    public String toString() {
        return "DiretorComercial{" +
                "setor='" + setor + '\'' +
                "} " + super.toString();
    }
}