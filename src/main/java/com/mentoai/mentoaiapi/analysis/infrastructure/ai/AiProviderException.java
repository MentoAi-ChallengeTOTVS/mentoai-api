package com.mentoai.mentoaiapi.analysis.infrastructure.ai;

import java.util.Set;

import org.springframework.http.HttpStatusCode;

public class AiProviderException extends RuntimeException {

    private static final Set<Integer> FALLBACK_HTTP_STATUS = Set.of(429, 500, 502, 503, 504);

    private final String provider;
    private final String model;
    private final Integer httpStatus;
    private final boolean fallbackElegivel;

    private AiProviderException(
            String message,
            String provider,
            String model,
            Integer httpStatus,
            boolean fallbackElegivel,
            Throwable cause
    ) {
        super(message, cause);
        this.provider = provider;
        this.model = model;
        this.httpStatus = httpStatus;
        this.fallbackElegivel = fallbackElegivel;
    }

    public static AiProviderException configuracao(String provider, String model, String descricao) {
        return new AiProviderException(
                "Configuração inválida para o provider " + provider + ": " + descricao,
                provider,
                model,
                null,
                false,
                null
        );
    }

    public static AiProviderException http(String provider, String model, HttpStatusCode status, Throwable cause) {
        int statusCode = status.value();
        return new AiProviderException(
                "Falha HTTP do provider " + provider + " (status " + statusCode + ")",
                provider,
                model,
                statusCode,
                FALLBACK_HTTP_STATUS.contains(statusCode),
                cause
        );
    }

    public static AiProviderException indisponibilidadeTemporaria(
            String provider,
            String model,
            Integer httpStatus,
            Throwable cause
    ) {
        return new AiProviderException(
                "Indisponibilidade temporária do provider " + provider,
                provider,
                model,
                httpStatus,
                true,
                cause
        );
    }

    public static AiProviderException respostaInvalida(String provider, String model) {
        return new AiProviderException(
                "Resposta inválida recebida do provider " + provider,
                provider,
                model,
                null,
                false,
                null
        );
    }

    public static AiProviderException integracao(String provider, String model, Throwable cause) {
        return new AiProviderException(
                "Falha não recuperável na integração com o provider " + provider,
                provider,
                model,
                null,
                false,
                cause
        );
    }

    public String getProvider() {
        return provider;
    }

    public String getModel() {
        return model;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public boolean isFallbackElegivel() {
        return fallbackElegivel;
    }
}
