package com.kltn.order_service.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private int status;
    private String message;
    private ProductDTO data;
}
