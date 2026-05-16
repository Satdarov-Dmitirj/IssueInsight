package ru.natification.mainup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.natification.mainup.dto.EmailNotificationDto;

@Service
public class EmailNotificationHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationHandler.class);

    private final MailService mailService;
    private final ObjectMapper objectMapper;

    public EmailNotificationHandler(MailService mailService) {
        this.mailService = mailService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "email_notifications", groupId = "notification-group")
    public void handleEmailNotification(String message) {
        System.out.println("=== KAFKA MESSAGE RECEIVED: " + message);
        logger.info("Received message from Kafka: {}", message);

        try {
            EmailNotificationDto notification = objectMapper.readValue(message, EmailNotificationDto.class);
            System.out.println("=== TYPE: " + notification.getType() + ", TO: " + notification.getTo());

            switch (notification.getType()) {
                case "TICKET_CREATED":
                    System.out.println("=== Sending TICKET_CREATED email to: " + notification.getTo());
                    mailService.sendTicketCreatedNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getSubject(),
                            notification.getTicketStatus()
                    );
                    break;

                case "STATUS_CHANGED":
                    System.out.println("=== Sending STATUS_CHANGED email to: " + notification.getTo());
                    mailService.sendTicketStatusChangeNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getTicketStatus()
                    );
                    break;

                case "TICKET_CLOSED":
                    System.out.println("=== Sending TICKET_CLOSED email to: " + notification.getTo());
                    mailService.sendTicketClosedNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getResolution()
                    );
                    break;

                default:
                    System.out.println("=== UNKNOWN TYPE: " + notification.getType());
                    logger.warn("Unknown notification type: {}", notification.getType());
            }

            System.out.println("=== EMAIL PROCESSING DONE");

        } catch (Exception e) {
            System.out.println("=== ERROR: " + e.getMessage());
            e.printStackTrace();
            logger.error("Failed to process email notification", e);
        }
    }
}