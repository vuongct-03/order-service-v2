package com.kltn.order_service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private Long id;
    private String specificationId;
    private Long userId;
    private String productId;
    private String productName;
    private String productAvatar;
    private int quantity;
    private double price;
    private double discountPercent;
}