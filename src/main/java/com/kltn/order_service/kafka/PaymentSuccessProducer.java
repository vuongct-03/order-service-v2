package com.kltn.order_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.order_service.kafka.dto.CommissionMoney;

@Service
public class PaymentSuccessProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Gửi dạng String

    private static final String TOPIC = "payment_success_event";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendCommissionMoney(String referralCode, double invoiceTotal) {
        try {
            
            CommissionMoney commissionMoney = CommissionMoney.builder()
                .referralCode(referralCode)
                .invoiceTotal(invoiceTotal)
            .build();

            // Chuyển object thành JSON string
            String responseJson = objectMapper.writeValueAsString(commissionMoney);
            kafkaTemplate.send(TOPIC, responseJson);
            System.out.println("Sent message to Kafka: " + responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
