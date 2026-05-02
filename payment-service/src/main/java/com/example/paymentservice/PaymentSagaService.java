package com.example.paymentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentSagaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "payment-saga-group")
    public void processPaymentForOrder(String message) {
        System.out.println("Payment Service processing event: " + message);
        String[] parts = message.split(",");
        if ("ORDER_CREATED".equals(parts[0])) {
            String orderId = parts[1];
            double price = Double.parseDouble(parts[2]);

            // Simulate business logic: Fail if price > 10000
            if (price > 10000) {
                System.out.println("Payment failed for Order ID: " + orderId + " due to insufficient funds.");
                kafkaTemplate.send("payment-events", "PAYMENT_FAILED," + orderId);
            } else {
                System.out.println("Payment successful for Order ID: " + orderId);
                kafkaTemplate.send("payment-events", "PAYMENT_SUCCESS," + orderId);
            }
        }
    }
}
