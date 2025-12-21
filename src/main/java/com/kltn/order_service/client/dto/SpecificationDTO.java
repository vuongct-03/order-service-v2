package com.kltn.order_service.client.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificationDTO {
    private String id;
    private String name;
    private double price;
    private String size;
    private String length;
    private String height;
    private String width;
    private String color;
    private int quantity;
    private String productId;
    private List<String> imageURLs;
    private ProductDTO product;
}
