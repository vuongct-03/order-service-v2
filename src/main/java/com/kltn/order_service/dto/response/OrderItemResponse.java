package com.kltn.order_service.dto.response;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {
    private Long id;
    private String specId;
    private String specName;
    private String image;
    private String productId;
    private int quantity;
    private double price;
    private Long orderId;
}
