package com.kltn.order_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.order_service.dto.response.OrderResponse;

@Service
public class OrderKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Gửi dạng String

    private static final String TOPIC = "order_created_v3";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOrderCreated(OrderResponse order) {
        try {
            // Chuyển object thành JSON string
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(TOPIC, orderJson);
            kafkaTemplate.flush(); // đảm bảo message được gửi ngay
            System.out.println("Sent message to Kafka: " + orderJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
