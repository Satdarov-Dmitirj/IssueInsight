package com.example.demo.service;

import com.example.demo.dto.EmailNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationSendlerService {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationSendlerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topics.email-notifications:email_notifications}")
    private String emailTopic;

    public void sendTicketCreatedNotification(String email, String ticketId, String subject, String status) {
        EmailNotificationDto notification = new EmailNotificationDto();
        notification.setTo(email);
        notification.setSubject(subject);
        notification.setType("TICKET_CREATED");
        notification.setTicketId(ticketId);
        notification.setTicketStatus(status);

        sendNotification(notification);
    }

    public void sendStatusChangeNotification(String email, String ticketId, String newStatus) {
        EmailNotificationDto notification = new EmailNotificationDto();
        notification.setTo(email);
        notification.setType("STATUS_CHANGED");
        notification.setTicketId(ticketId);
        notification.setTicketStatus(newStatus);

        sendNotification(notification);
    }

    public void sendTicketClosedNotification(String email, String ticketId, String resolution) {
        EmailNotificationDto notification = new EmailNotificationDto();
        notification.setTo(email);
        notification.setType("TICKET_CLOSED");
        notification.setTicketId(ticketId);
        notification.setResolution(resolution);

        sendNotification(notification);
    }

    private void sendNotification(EmailNotificationDto notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(emailTopic, message);
            logger.info("Email notification sent to Kafka: type={}, to={}",
                    notification.getType(), notification.getTo());
        } catch (Exception e) {
            logger.error("Failed to send email notification to Kafka", e);
        }
    }
}