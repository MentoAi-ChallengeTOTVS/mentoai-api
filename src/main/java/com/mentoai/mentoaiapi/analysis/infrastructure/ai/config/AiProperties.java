package com.mentoai.mentoaiapi.analysis.infrastructure.ai.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mentoai.ai")
public class AiProperties {

    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(60);
    private final Gemini gemini = new Gemini();
    private final Groq groq = new Groq();

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Gemini getGemini() {
        return gemini;
    }

    public Groq getGroq() {
        return groq;
    }

    public static class Gemini {

        private String baseUrl = "https://generativelanguage.googleapis.com";
        private String apiKey;
        private String primaryModel = "gemini-3.5-flash-lite";
        private String fallbackModel = "gemini-3.5-flash";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getPrimaryModel() {
            return primaryModel;
        }

        public void setPrimaryModel(String primaryModel) {
            this.primaryModel = primaryModel;
        }

        public String getFallbackModel() {
            return fallbackModel;
        }

        public void setFallbackModel(String fallbackModel) {
            this.fallbackModel = fallbackModel;
        }
    }

    public static class Groq {

        private String baseUrl = "https://api.groq.com";
        private String apiKey;
        private String model;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }
}
