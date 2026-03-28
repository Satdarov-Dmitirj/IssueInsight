package ru.natification.mainup.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Hendlier {
    private final String topik = "email_topik";

    @KafkaListener(topics = topik)
    public void hendlUser(String message){
        System.out.println("Пришло сообщение от User от другого модуля" + message);
    }
}
