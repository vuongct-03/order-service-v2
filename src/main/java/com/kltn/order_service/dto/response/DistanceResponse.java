package com.kltn.order_service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DistanceResponse {
    private String destinationAddresses;
    private String originAddresses;
    private String distanse;
    private String duration;
}
