package com.kltn.order_service.dto.GHN;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GHNCreateOrderResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private GHNData data;

    @Builder
    @Getter
    public static class GHNData {
        @JsonProperty("order_code")
        private String orderCode;

        @JsonProperty("trans_type")
        private String transType;

        @JsonProperty("total_fee")
        private int totalFee;

        @JsonProperty("expected_delivery_time")
        private String expectedDeliveryTime;
    }
}
