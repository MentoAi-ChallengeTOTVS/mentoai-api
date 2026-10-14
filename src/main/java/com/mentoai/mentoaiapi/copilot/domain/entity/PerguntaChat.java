package com.mentoai.mentoaiapi.copilot.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;

public class PerguntaChat {
    private Long id;
    private Chat chat;
    private String pergunta;
    private String resposta;
    private IntencaoCopiloto intencao;
    private List<FonteCopiloto> fontes;
    private LocalDateTime criacao;

    public PerguntaChat() {
    }

    public PerguntaChat(Long id, Chat chat, String pergunta, String resposta, IntencaoCopiloto intencao,
                       List<FonteCopiloto> fontes, LocalDateTime criacao) {
        this.id = id;
        this.chat = chat;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.intencao = intencao;
        this.fontes = fontes == null ? List.of() : List.copyOf(fontes);
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
    public IntencaoCopiloto getIntencao() { return intencao; }
    public void setIntencao(IntencaoCopiloto intencao) { this.intencao = intencao; }
    public List<FonteCopiloto> getFontes() { return fontes == null ? List.of() : fontes; }
    public void setFontes(List<FonteCopiloto> fontes) { this.fontes = List.copyOf(fontes); }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }

    @Override
    public String toString() {
        return "PerguntaChat{" + "id=" + id + ", pergunta='" + pergunta + '\'' + ", criacao=" + criacao + '}';
    }
}
