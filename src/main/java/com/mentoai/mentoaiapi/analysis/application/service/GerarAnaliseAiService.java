package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.application.ai.AnaliseComercialPrompt;
import com.mentoai.mentoaiapi.analysis.application.ai.ResultadoAnaliseAiParser;
import com.mentoai.mentoaiapi.analysis.application.dto.EntradaAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.dto.ResultadoAnaliseAi;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiRequest;
import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import org.springframework.stereotype.Service;

@Service
public class GerarAnaliseAiService {

    private final AiProvider aiProvider;
    private final AnaliseComercialPrompt prompt;
    private final ResultadoAnaliseAiParser parser;

    public GerarAnaliseAiService(AiProvider aiProvider, AnaliseComercialPrompt prompt, ResultadoAnaliseAiParser parser) {
        this.aiProvider = aiProvider;
        this.prompt = prompt;
        this.parser = parser;
    }

    public ResultadoAnaliseAi gerar(EntradaAnaliseAi entrada) {
        AiRequest request = prompt.criarRequisicao(entrada);
        AiResponse response = aiProvider.gerar(request);
        return parser.parse(response, entrada);
    }
}
