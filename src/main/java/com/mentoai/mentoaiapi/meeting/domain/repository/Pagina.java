package com.mentoai.mentoaiapi.meeting.domain.repository;

import java.util.List;

public record Pagina<T>(List<T> conteudo,int pagina,int tamanho,long totalElementos,int totalPaginas) {
}