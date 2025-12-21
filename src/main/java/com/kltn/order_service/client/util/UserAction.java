package com.kltn.order_service.client.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserAction {
    @JsonProperty("view")
    VIEW,

    @JsonProperty("add_to_cart")
    ADD_TO_CART,

    @JsonProperty("buy")
    BUY
}
