package com.mentoai.mentoaiapi.copilot.application.ai;

import com.mentoai.mentoaiapi.analysis.application.port.ai.AiResponse;
import com.mentoai.mentoaiapi.copilot.application.dto.ContextoCopiloto;
import com.mentoai.mentoaiapi.copilot.domain.enums.IntencaoCopiloto;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CopilotPromptTest {
    private final CopilotPrompt prompt = new CopilotPrompt(new com.mentoai.mentoaiapi.copilot.application.service.CopilotContextProperties());

    @Test void criaJsonEstruturadoEValidaResposta() {
        var contexto = new ContextoCopiloto(new ContextoCopiloto.ClienteContexto(1L, "Atlas", null, null, null),
                List.of(), List.of(), List.of(), List.of(), List.of());
        var request = prompt.criar(contexto, List.of(), IntencaoCopiloto.CLIENT_RISK, "Quais riscos?");
        assertTrue(request.prompt().contains("Quais riscos?"));
        assertTrue(request.prompt().contains("Atlas"));
        assertEquals("object", request.responseSchema().get("type"));
        assertEquals("Há um risco.", prompt.resposta(new AiResponse("{\"resposta\":\"Há um risco.\"}", "GEMINI", "m")));
        assertThrows(CopilotResponseException.class,
                () -> prompt.resposta(new AiResponse("{\"outro\":1}", "GEMINI", "m")));
    }
}
