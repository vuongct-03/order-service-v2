package com.kltn.order_service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShippingStatus {

    @JsonProperty("processing ")
    PROCESSING,

    @JsonProperty("in_transit")
    IN_TRANSIT,

    @JsonProperty("delivered")
    DELIVERED
}
