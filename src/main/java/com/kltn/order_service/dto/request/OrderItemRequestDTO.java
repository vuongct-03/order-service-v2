package com.kltn.order_service.dto.request;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class OrderItemRequestDTO {
    private String specId;
    private int quantity;
    private double discountPercent;
}
