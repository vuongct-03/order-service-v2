package com.kltn.order_service.dto.request;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequestDTO {
    private Long userId;
    private String orderCode;
    private String transType;
    private int totalShippingFee;
    private String expectedDeliveryTime;
    private String shippingAddress;
    private boolean isPayment;
    private String referralCode;
    private Set<OrderItemRequestDTO> orderItems;
}
