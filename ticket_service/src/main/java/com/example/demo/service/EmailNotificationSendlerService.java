package com.example.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailNotificationSendlerService {
    KafkaTemplate <String, String> kafkaTemplate;

    public void sendMessage(String topik, String message){
        kafkaTemplate.send(topik, message);
    }
}
