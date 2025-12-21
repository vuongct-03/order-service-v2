package com.kltn.order_service.dto.GHN;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderRequest {
    @JsonProperty("order_codes")
    private List<String> orderCodes;
}
