package com.kltn.order_service.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDTO {

    private String id;
    private String name;

    private String avatarImage;

    private String description;

    private double discountPercent;
}
