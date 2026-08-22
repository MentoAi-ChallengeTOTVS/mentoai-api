package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERGUNTA_CHAT")
public class PerguntaChatJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CHAT_ID", nullable = false)
    private ChatJpaEntity chat;

    @Lob
    @Column(name = "PERGUNTA", nullable = false, columnDefinition = "CLOB")
    private String pergunta;

    @Lob
    @Column(name = "RESPOSTA", columnDefinition = "CLOB")
    private String resposta;

    @Column(name = "CRIACAO", nullable = false)
    private LocalDateTime criacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ChatJpaEntity getChat() { return chat; }
    public void setChat(ChatJpaEntity chat) { this.chat = chat; }
    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }
    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
    public LocalDateTime getCriacao() { return criacao; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
}
