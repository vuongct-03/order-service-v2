package com.kltn.order_service.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.order_service.kafka.dto.ProductResponseKafka;

import com.kltn.order_service.service.CartItemService;

@Service
public class ProductUpdateConsumer {
    @Autowired
    private CartItemService cartItemService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product_update_event", groupId = "order_group")
    public void consumeSpecificationUpdate(String message) {
        try {
            System.err.println("❌  Kafka message update spec ok: " );
            // Parse JSON thành object
            ProductResponseKafka product = objectMapper.readValue(message, ProductResponseKafka.class);

            //Cập nhật spec trong cartItem
            cartItemService.updateProductForCartItemKafka(product);

        } catch (Exception e) {
            System.err.println("❌ Failed to parse Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
