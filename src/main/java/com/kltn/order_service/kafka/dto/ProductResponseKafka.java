package com.kltn.order_service.kafka.dto;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ProductResponseKafka implements Serializable {
    private String id;
    private String name;
    private double discountPercent;
}

