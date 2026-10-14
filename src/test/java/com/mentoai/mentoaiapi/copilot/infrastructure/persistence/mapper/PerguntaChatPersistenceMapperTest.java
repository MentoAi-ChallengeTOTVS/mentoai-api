package com.mentoai.mentoaiapi.copilot.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.copilot.domain.entity.*;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import com.mentoai.mentoaiapi.copilot.domain.enums.TipoFonteCopiloto;
import com.mentoai.mentoaiapi.copilot.infrastructure.persistence.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PerguntaChatPersistenceMapperTest {
    @Test void serializaEDesserializaFontesVersionadas() {
        var chatMapper = mock(ChatPersistenceMapper.class);
        var mapper = new PerguntaChatPersistenceMapper(chatMapper);
        var chat = new Chat(); chat.setId(3L);
        var fonte = new FonteCopiloto(TipoFonteCopiloto.SINAL_COMERCIAL, 52L, 15L, 18L);
        var pergunta = new PerguntaChat(1L, chat, "Riscos?", "Risco identificado", IntencaoCopiloto.CLIENT_RISK,
                List.of(fonte), LocalDateTime.now());
        var entity = mapper.toJpaEntity(pergunta, new ChatJpaEntity());
        assertTrue(entity.getFontesJson().contains("\"versao\":1"));
        when(chatMapper.toDomain(entity.getChat())).thenReturn(chat);
        var roundTrip = mapper.toDomain(entity);
        assertEquals(List.of(fonte), roundTrip.getFontes());
        assertEquals(IntencaoCopiloto.CLIENT_RISK, roundTrip.getIntencao());
    }
}
