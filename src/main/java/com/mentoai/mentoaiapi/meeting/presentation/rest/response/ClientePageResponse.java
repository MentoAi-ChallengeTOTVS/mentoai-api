package com.mentoai.mentoaiapi.meeting.presentation.rest.response;

import java.util.List;

public record ClientePageResponse(List<ClienteResponse> conteudo,int pagina, int tamanho, long totalElementos, int totalPaginas) {
}