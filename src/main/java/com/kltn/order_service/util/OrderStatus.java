package com.kltn.order_service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {

    @JsonProperty("pendding")
    PENDDING,

    @JsonProperty("shipping")
    SHIPPING,

    @JsonProperty("delivered")
    DELIVERED,

    @JsonProperty("cancelled")
    CANCELLED;
}
