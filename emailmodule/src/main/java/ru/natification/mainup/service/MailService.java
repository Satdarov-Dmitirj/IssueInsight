package ru.natification.mainup.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${mail.service.enabled:true}")
    private boolean mailEnabled;

    @Value("${mail.service.from}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
            logger.info("Отправка почты отключена (mail.service.enabled=false)");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            helper.setFrom(fromEmail);
            mailSender.send(message);
            logger.info("Email отправлен на: {}", to);
        } catch (MessagingException e) {
            logger.error("Ошибка отправки email на: {}", to, e);
        }
    }

    public void sendTicketCreatedNotification(String to, String ticketId, String subject, String status) {
        String emailSubject = "Тикет #" + ticketId + " создан";
        String body = "Ваше обращение принято.\n\n"
                + "Номер тикета: #" + ticketId + "\n"
                + "Тема: " + subject + "\n"
                + "Статус: " + status + "\n\n"
                + "Мы свяжемся с вами как только обработаем ваше обращение.";
        sendEmail(to, emailSubject, body);
    }

    public void sendTicketStatusChangeNotification(String to, String ticketId, String newStatus) {
        String subject = "Тикет #" + ticketId + " — статус изменён";
        String body = "Статус вашего обращения #" + ticketId + " изменён.\n\n"
                + "Новый статус: " + newStatus + "\n\n"
                + "Следите за обновлениями в системе IssueInsight.";
        sendEmail(to, subject, body);
    }

    public void sendTicketClosedNotification(String to, String ticketId, String resolution) {
        String subject = "Тикет #" + ticketId + " закрыт";
        String body = "Ваше обращение #" + ticketId + " закрыто.\n\n"
                + "Решение: " + (resolution != null ? resolution : "—") + "\n\n"
                + "Спасибо за обращение в службу поддержки IssueInsight.";
        sendEmail(to, subject, body);
    }
}