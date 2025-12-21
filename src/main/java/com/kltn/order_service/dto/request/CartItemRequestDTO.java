package com.kltn.order_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemRequestDTO {
    private String specId;
    private Long userId;
    private int quantity;
}
