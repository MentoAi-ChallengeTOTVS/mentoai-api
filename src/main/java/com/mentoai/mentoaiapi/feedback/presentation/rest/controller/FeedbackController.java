package com.mentoai.mentoaiapi.feedback.presentation.rest.controller;

import com.mentoai.mentoaiapi.feedback.application.service.FeedbackService;
import com.mentoai.mentoaiapi.feedback.presentation.rest.request.CriarFeedbackRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(
            @Valid @RequestBody CriarFeedbackRequest request,
            Authentication authentication) {

        String usuarioEmail = authentication.getName();
        feedbackService.processarFeedback(
                request.nota(),
                request.comentario(),
                usuarioEmail,
                request.emailCopy()
        );

        return ResponseEntity.ok().build();
    }
}