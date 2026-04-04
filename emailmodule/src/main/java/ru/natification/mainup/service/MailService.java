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

    @Value("mail.service.enabled:true")
    private boolean mailEnabled;

    @Value("mail.service.from")
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
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
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", to, e);
        }
    }

    public void sendTicketCreatedNotification(String to, String ticketId, String subject, String status) {
        String emailSubject = "Ticket " + ticketId + " created";
        String body = "Ticket " + ticketId + " created" +
                "Subject: " + subject + " " +
                "Status: " + status;
        sendEmail(to, emailSubject, body);
    }

    public void sendTicketStatusChangeNotification(String to, String ticketId, String newStatus) {
        String subject = "Ticket #" + ticketId + " status update";
        String body = "Ticket #" + ticketId + " status changed to: " + newStatus;
        sendEmail(to, subject, body);
    }

    public void sendTicketClosedNotification(String to, String ticketId, String resolution) {
        String subject = "Ticket " + ticketId + " closed";
        String body = "Ticket " + ticketId + " closed" +
                "Resolution: " + (resolution != null ? resolution : "None");
        sendEmail(to, subject, body);
    }
}