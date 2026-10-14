package com.mentoai.mentoaiapi.shared.web;

import com.mentoai.mentoaiapi.shared.exception.BusinessException;
import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import com.mentoai.mentoaiapi.analysis.infrastructure.ai.AiProviderException;
import com.mentoai.mentoaiapi.copilot.application.ai.CopilotResponseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            RuntimeException exception, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request, null);
    }

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class, ConstraintViolationException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> badRequest(Exception exception, HttpServletRequest request) {
        String message = exception instanceof BusinessException ? exception.getMessage() : "Requisição inválida";
        return build(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException exception,
                                                     HttpServletRequest request) {
        var fields = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldErrorResponse(e.getField(), e.getDefaultMessage())).toList();
        return build(HttpStatus.BAD_REQUEST, "Requisição inválida", request, fields);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), request, null);
    }

    @ExceptionHandler({AiProviderException.class, CopilotResponseException.class})
    public ResponseEntity<ErrorResponse> provider(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de IA indisponível", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        LOGGER.error("Erro inesperado ao processar {}", request.getRequestURI(), exception);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request, null);
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<FieldErrorResponse> fieldErrors) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors);
        return ResponseEntity.status(status).body(response);
    }
}
