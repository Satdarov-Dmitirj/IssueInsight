package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${mail.service.enabled:true}")
    private boolean mailEnabled;

    @Value("${spring.mail.username:#{null}}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
            logger.warn("Mail service is disabled. Would send email to: {}, subject: {}", to, subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // HTML content enabled
            helper.setFrom(fromEmail != null ? fromEmail : "noreply@issueinsight.com");

            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendTicketNotification(String userEmail, String ticketId, String ticketStatus, String resolution) {
        String subject = "Обновление статуса тикета " + ticketId;
        String body = """
            <html>
            <body>
                <h2>Обновление статуса тикета</h2>
                <p>Ваш тикет <strong>#%s</strong> был обновлен.</p>
                <p><strong>Статус:</strong> %s</p>
                %s
                <p>Спасибо за использование нашей системы поддержки!</p>
            </body>
            </html>
            """.formatted(ticketId, ticketStatus,
                resolution != null ? "<p><strong>Решение:</strong> " + resolution + "</p>" : "");

        sendEmail(userEmail, subject, body);
    }
}