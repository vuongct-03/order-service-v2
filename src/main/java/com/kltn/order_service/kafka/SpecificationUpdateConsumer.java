package com.kltn.order_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.order_service.kafka.dto.SpecificationResponseKafka;
import com.kltn.order_service.service.CartItemService;

@Service
public class SpecificationUpdateConsumer {
    @Autowired
    private CartItemService cartItemService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "specification_update_event", groupId = "order_group")
    public void consumeSpecificationUpdate(String message) {
        try {
            System.err.println("❌  Kafka message update spec ok: " );
            // Parse JSON thành object
            SpecificationResponseKafka spec = objectMapper.readValue(message, SpecificationResponseKafka.class);

            //Cập nhật spec trong cartItem
            cartItemService.updateCartItemKafka(spec);

        } catch (Exception e) {
            System.err.println("❌ Failed to parse Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
