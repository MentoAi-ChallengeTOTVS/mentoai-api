package com.mentoai.mentoaiapi.copilot.application.service;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiProvider;
import com.mentoai.mentoaiapi.copilot.application.ai.CopilotPrompt;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.shared.exception.BusinessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CopilotService {
    private final ChatService chats;
    private final PerguntaChatService perguntas;
    private final IntentResolver intents;
    private final ContextRetriever retriever;
    private final ContextAssembler assembler;
    private final CopilotPrompt prompt;
    private final AiProvider provider;
    private final CopilotContextProperties limits;

    public CopilotService(ChatService chats, PerguntaChatService perguntas, IntentResolver intents,
                          ContextRetriever retriever, ContextAssembler assembler, CopilotPrompt prompt,
                          @Qualifier("copilotAiProvider") AiProvider provider, CopilotContextProperties limits) {
        this.chats = chats;
        this.perguntas = perguntas;
        this.intents = intents;
        this.retriever = retriever;
        this.assembler = assembler;
        this.prompt = prompt;
        this.provider = provider;
        this.limits = limits;
    }

    public PerguntaChat perguntar(Long chatId, String email, String pergunta) {
        var chat = chats.buscarPorId(chatId, email);
        if (chat.getCliente() == null) {
            throw new BusinessException("Este Chat não está associado a um cliente");
        }
        var intent = intents.resolver(pergunta);
        var contexto = assembler.montar(retriever.recuperar(chat.getCliente().getId(), intent.detectadas()));
        var historico = perguntas.historicoRecente(chatId, limits.getHistorico());
        var request = prompt.criar(contexto, historico, intent.principal(), pergunta);
        var resposta = prompt.resposta(provider.gerar(request));
        return perguntas.salvarConcluida(chat, pergunta, resposta, intent.principal(), contexto.fontes());
    }
}
