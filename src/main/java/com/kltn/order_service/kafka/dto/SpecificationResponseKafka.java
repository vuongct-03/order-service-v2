package com.kltn.order_service.kafka.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationResponseKafka implements Serializable {
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
}

