package com.mentoai.mentoaiapi.copilot.application.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mentoai.copilot.context")
public class CopilotContextProperties {
    private int reunioes = 5;
    private int insights = 5;
    private int sinais = 8;
    private int alertas = 5;
    private int historico = 6;
    private int tamanhoCampo = 1200;
    public int getReunioes() { return reunioes; }
    public void setReunioes(int value) { reunioes = positive(value); }
    public int getInsights() { return insights; }
    public void setInsights(int value) { insights = positive(value); }
    public int getSinais() { return sinais; }
    public void setSinais(int value) { sinais = positive(value); }
    public int getAlertas() { return alertas; }
    public void setAlertas(int value) { alertas = positive(value); }
    public int getHistorico() { return historico; }
    public void setHistorico(int value) { historico = positive(value); }
    public int getTamanhoCampo() { return tamanhoCampo; }
    public void setTamanhoCampo(int value) { tamanhoCampo = positive(value); }
    private int positive(int value) {
        if (value < 1 || value > 10000) throw new IllegalArgumentException("Limite do contexto inválido");
        return value;
    }
}
