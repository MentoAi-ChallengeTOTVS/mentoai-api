package com.mentoai.mentoaiapi.copilot.domain.entity;

import java.time.LocalDateTime;

public class PerguntaChat {
    private Long id;
    private Chat chat;
    private String pergunta;
    private String resposta;
    private LocalDateTime criacao;

    public PerguntaChat() {
    }

    public PerguntaChat(Long id, Chat chat, String pergunta, String resposta, LocalDateTime criacao) {
        this.id = id;
        this.chat = chat;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.criacao = criacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }
    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }
    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "PerguntaChat{" + "id=" + id + ", pergunta='" + pergunta + '\'' + ", criacao=" + criacao + '}';
    }
}
