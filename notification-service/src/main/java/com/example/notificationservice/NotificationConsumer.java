package com.example.notificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consume(String message) {
        System.out.println("Notification Service received message: " + message);
        // Simulate sending email/SMS here
    }
}
