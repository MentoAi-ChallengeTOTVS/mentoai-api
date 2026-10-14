package com.mentoai.mentoaiapi.copilot.application.ai;

public class CopilotResponseException extends RuntimeException {
    public CopilotResponseException(Throwable cause) { super("Resposta do provider inválida", cause); }
}
