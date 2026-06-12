package com.mentoai.mentoaiapi.analysis.domain.entity;

import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;

import java.util.ArrayList;
import java.util.List;

public class AnaliseIA {
    private Long id;

    private String resumoExecutivo;

    private SentimentoGeral sentimentoGeral;

    private Double riscoChurn;

    private Reuniao reuniao;


    private List<Insight> insights = new ArrayList<>();
    public AnaliseIA(){

    }

    public AnaliseIA(Long id, String resumoExecutivo, SentimentoGeral sentimentoGeral, Double riscoChurn, Reuniao reuniao) {
        this.id = id;
        this.resumoExecutivo = resumoExecutivo;
        this.sentimentoGeral = sentimentoGeral;
        this.riscoChurn = riscoChurn;
        this.reuniao = reuniao;
    }


    public void adicionarInsight(Insight insight){
        insights.add(insight);
    }

    public String gerarResumoExecutivo() {

        return resumoExecutivo;

    }

    public List<Insight> gerarInsights() {
        return insights;
    }

    @Override
    public String toString() {
        return "AnaliseIA{" +
                "id=" + id +
                ", sentimentoGeral=" + sentimentoGeral +
                ", riscoChurn=" + riscoChurn +
                ", resumoExecutivo='" + resumoExecutivo + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumoExecutivo() {
        return resumoExecutivo;
    }

    public void setResumoExecutivo(String resumoExecutivo) {
        this.resumoExecutivo = resumoExecutivo;
    }

    public SentimentoGeral getSentimentoGeral() {
        return sentimentoGeral;
    }

    public void setSentimentoGeral(SentimentoGeral sentimentoGeral) {
        this.sentimentoGeral = sentimentoGeral;
    }

    public Double getRiscoChurn() {
        return riscoChurn;
    }

    public void setRiscoChurn(Double riscoChurn) {
        this.riscoChurn = riscoChurn;
    }

    public Reuniao getReuniao() {
        return reuniao;
    }

    public void setReuniao(Reuniao reuniao) {
        this.reuniao = reuniao;
    }
}
