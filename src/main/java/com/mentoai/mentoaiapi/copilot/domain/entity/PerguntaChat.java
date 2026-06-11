package com.mentoai.mentoaiapi.copilot.domain.entity;

public class PerguntaChat {

    private Long id;
    private String pergunta;
    private String resposta;
    private Chat chat;

    public PerguntaChat() {
    }

    public PerguntaChat(
            Long id,
            String pergunta,
            String resposta,
            Chat chat
    ) {
        this.id = id;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.chat = chat;
    }

    public String gerarResposta() {
        return resposta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "PerguntaChat{" +
                "id=" + id +
                ", pergunta='" + pergunta + '\'' +
                '}';
    }
}