package com.kltn.order_service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoordinateReponse {
    private String lat;
    private String lon;
    private String address;
}
