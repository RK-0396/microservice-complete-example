package com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderSagaConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "payment-events", groupId = "order-saga-group")
    public void consumePaymentEvent(String message) {
        System.out.println("Order Service received payment event: " + message);
        String[] parts = message.split(",");
        String eventType = parts[0];
        Long orderId = Long.parseLong(parts[1]);

        orderRepository.findById(orderId).ifPresent(order -> {
            if ("PAYMENT_SUCCESS".equals(eventType)) {
                order.setStatus("COMPLETED");
            } else if ("PAYMENT_FAILED".equals(eventType)) {
                order.setStatus("FAILED"); // Rollback
            }
            orderRepository.save(order);
            System.out.println("Order " + orderId + " status updated to " + order.getStatus());
        });
    }
}
