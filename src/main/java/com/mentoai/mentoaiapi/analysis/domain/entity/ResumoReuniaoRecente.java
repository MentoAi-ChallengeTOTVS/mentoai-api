package com.mentoai.mentoaiapi.analysis.domain.entity;

import java.time.LocalDateTime;

public record ResumoReuniaoRecente(LocalDateTime dataReuniao, String resumoExecutivo) {
}
