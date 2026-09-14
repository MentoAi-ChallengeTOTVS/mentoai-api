package com.mentoai.mentoaiapi.feedback.application.service;

import com.mentoai.mentoaiapi.shared.infrastructure.mail.EmailService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final EmailService emailService;

    public FeedbackService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void processarFeedback(Integer nota, String comentario, String usuarioEmail, String emailCopy) {
        // Envia o e-mail de agradecimento ao usuário que avaliou
        emailService.enviarAgradecimentoCliente(usuarioEmail, nota);

        // Envia o e-mail com os detalhes do feedback para a equipe interna
        emailService.enviarNotificacaoEquipe(usuarioEmail, nota, comentario, emailCopy);
    }
}