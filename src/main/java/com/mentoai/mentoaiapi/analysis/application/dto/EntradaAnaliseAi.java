package com.mentoai.mentoaiapi.analysis.application.dto;

import java.util.Objects;

public record EntradaAnaliseAi(ClienteContexto cliente, TranscricaoConteudo transcricao) {

    public EntradaAnaliseAi {
        Objects.requireNonNull(cliente, "O contexto do cliente é obrigatório");
        Objects.requireNonNull(transcricao, "A transcrição é obrigatória");
    }

    public record ClienteContexto(String nome, String segmento, String porte) {

        public ClienteContexto {
            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("O nome do cliente é obrigatório");
            }
            if (segmento == null || segmento.isBlank()) {
                throw new IllegalArgumentException("O segmento do cliente é obrigatório");
            }
            if (porte == null || porte.isBlank()) {
                throw new IllegalArgumentException("O porte do cliente é obrigatório");
            }
        }
    }

    public record TranscricaoConteudo(String conteudo) {

        public TranscricaoConteudo {
            if (conteudo == null || conteudo.isBlank()) {
                throw new IllegalArgumentException("O conteúdo da transcrição é obrigatório");
            }
        }
    }
}
