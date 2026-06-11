package com.mentoai.mentoaiapi.user.domain.entity;

public class ExecutivoComercial extends Usuario {
        private String equipe;

        public ExecutivoComercial() {
        }

        public ExecutivoComercial(
                Long id,
                String nome,
                String email,
                String senha,
                String equipe
        ) {
            super(id, nome, email, senha);
            this.equipe = equipe;
        }

        public void consultarCliente() {
            System.out.println("Consultando informações do cliente...");
        }

        public String getEquipe() {
            return equipe;
        }

        public void setEquipe(String equipe) {
            this.equipe = equipe;
        }

        @Override
        public String toString() {
            return "ExecutivoComercial{" +
                    "equipe='" + equipe + '\'' +
                    "} " + super.toString();
        }
    }
