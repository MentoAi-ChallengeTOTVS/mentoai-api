package com.mentoai.mentoaiapi.analysis.application.port.ai;

public interface AiProvider {

    AiResponse gerar(AiRequest request);
}
