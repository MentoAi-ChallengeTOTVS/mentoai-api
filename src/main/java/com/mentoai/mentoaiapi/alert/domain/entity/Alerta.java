package com.mentoai.mentoaiapi.alert.domain.entity;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;

    public class Alerta {

        private Long id;
        private String descricao;
        private PrioridadeAlerta prioridade;
        private AnaliseIA analise;

        public Alerta() {
        }

        public Alerta(
                Long id,
                String descricao,
                PrioridadeAlerta prioridade,
                AnaliseIA analise
        ) {
            this.id = id;
            this.descricao = descricao;
            this.prioridade = prioridade;
            this.analise = analise;
        }

        public void avaliarPrioridade() {
            System.out.println("Avaliando prioridade do alerta...");
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public PrioridadeAlerta getPrioridade() {
            return prioridade;
        }

        public void setPrioridade(PrioridadeAlerta prioridade) {
            this.prioridade = prioridade;
        }

        public AnaliseIA getAnalise() {
            return analise;
        }

        public void setAnalise(AnaliseIA analise) {
            this.analise = analise;
        }

        @Override
        public String toString() {
            return "Alerta{" +
                    "id=" + id +
                    ", descricao='" + descricao + '\'' +
                    ", prioridade=" + prioridade +
                    '}';
        }
    }