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
        logger.info("Received message from Kafka: {}", message);

        try {
            EmailNotificationDto notification = objectMapper.readValue(message, EmailNotificationDto.class);

            switch (notification.getType()) {
                case "TICKET_CREATED":
                    mailService.sendTicketCreatedNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getSubject(),
                            notification.getTicketStatus()
                    );
                    break;

                case "STATUS_CHANGED":
                    mailService.sendTicketStatusChangeNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getTicketStatus()
                    );
                    break;

                case "TICKET_CLOSED":
                    mailService.sendTicketClosedNotification(
                            notification.getTo(),
                            notification.getTicketId(),
                            notification.getResolution()
                    );
                    break;

                default:
                    logger.warn("Unknown notification type:", notification.getType());
            }

        } catch (Exception e) {
            logger.error("Failed to process email notification", e);
        }
    }
}