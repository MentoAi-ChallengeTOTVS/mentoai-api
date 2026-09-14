package com.mentoai.mentoaiapi.shared.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${mentoai.mail.feedback-recipient:suporte@mentoai.com.br}")
    private String emailEquipe;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarAgradecimentoCliente(String emailCliente, Integer nota) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(emailCliente);
            helper.setSubject("Mensagem recebida com sucesso! | MentoAI");

            String htmlBody = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f4f5; margin: 0; padding: 30px 10px;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td align="center">
                                <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 12px; padding: 40px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: left;">
                                    <tr>
                                        <td style="padding-bottom: 24px; border-bottom: 1px solid #e4e4e7;">
                                            <h2 style="color: #0f172a; margin: 0; font-size: 22px; font-weight: 700; letter-spacing: -0.5px;">
                                                Mento<span style="color: #10b981;">AI</span>
                                            </h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 32px 0;">
                                            <h3 style="color: #0f172a; margin-top: 0; font-size: 18px; font-weight: 600;">Sua mensagem foi entregue com sucesso!</h3>
                                            <p style="color: #475569; font-size: 15px; line-height: 1.6; margin-bottom: 16px;">Olá,</p>
                                            <p style="color: #475569; font-size: 15px; line-height: 1.6; margin-bottom: 16px;">
                                                Confirmamos o recebimento da sua avaliação sobre o nosso copiloto comercial. 
                                            </p>
                                            <p style="color: #475569; font-size: 15px; line-height: 1.6; margin-bottom: 0;">
                                                Agradecemos imensamente pelo tempo disponibilizado em compartilhar sua experiência conosco. Seu retorno é fundamental para continuarmos evoluindo a plataforma!
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="border-top: 1px solid #e4e4e7; padding-top: 24px; text-align: center;">
                                            <p style="color: #94a3b8; font-size: 13px; margin: 0; line-height: 1.5;">
                                                Atenciosamente,<br>
                                                <strong style="color: #64748b;">Equipe MentoAI</strong>
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """;

            helper.setText(htmlBody, true);
            mailSender.send(message);
            LOGGER.info("E-mail HTML de agradecimento enviado com sucesso para {}", emailCliente);

        } catch (MessagingException e) {
            LOGGER.error("Erro ao enviar e-mail HTML de agradecimento para {}", emailCliente, e);
        }
    }

    @Async
    public void enviarNotificacaoEquipe(String emailCliente, Integer nota, String comentario) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(emailEquipe);
            helper.setSubject("Novo Feedback Recebido (" + nota + "/5 ⭐) | MentoAI");

            String comentarioFormatado = (comentario != null && !comentario.isBlank())
                    ? comentario.replace("\n", "<br>")
                    : "<em style='color: #94a3b8;'>Nenhum comentário preenchido pelo usuário.</em>";

            String htmlBody = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f4f5; margin: 0; padding: 30px 10px;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td align="center">
                                <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 12px; padding: 40px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: left;">
                                    <tr>
                                        <td style="padding-bottom: 24px; border-bottom: 1px solid #e4e4e7;">
                                            <h2 style="color: #0f172a; margin: 0; font-size: 22px; font-weight: 700; letter-spacing: -0.5px;">
                                                Mento<span style="color: #10b981;">AI</span>
                                            </h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 24px 0;">
                                            <h3 style="color: #0f172a; margin-top: 0; font-size: 18px; font-weight: 600;">Novo Feedback Recebido</h3>
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-bottom: 20px;">
                                                <tr>
                                                    <td style="padding: 8px 0; color: #475569; font-size: 14px; width: 100px;"><strong>Usuário:</strong></td>
                                                    <td style="padding: 8px 0; color: #0f172a; font-size: 14px;">{{emailCliente}}</td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 8px 0; color: #475569; font-size: 14px;"><strong>Nota:</strong></td>
                                                    <td style="padding: 8px 0; color: #10b981; font-size: 16px; font-weight: 700;">{{nota}} / 5 ⭐</td>
                                                </tr>
                                            </table>
                                            
                                            <p style="color: #475569; font-size: 14px; font-weight: 600; margin-bottom: 8px;">Comentário:</p>
                                            <div style="background-color: #f8fafc; border-left: 4px solid #10b981; padding: 16px; border-radius: 4px; color: #334155; font-size: 14px; line-height: 1.6;">
                                                {{comentario}}
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """
                    .replace("{{emailCliente}}", emailCliente)
                    .replace("{{nota}}", String.valueOf(nota))
                    .replace("{{comentario}}", comentarioFormatado);

            helper.setText(htmlBody, true);
            mailSender.send(message);
            LOGGER.info("E-mail de notificação de feedback enviado com sucesso para a equipe ({})", emailEquipe);

        } catch (MessagingException e) {
            LOGGER.error("Erro ao enviar e-mail de notificação de feedback para a equipe", e);
        }
    }
}