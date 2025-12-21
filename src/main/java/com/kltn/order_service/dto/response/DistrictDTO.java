package com.kltn.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DistrictDTO {
    @JsonProperty("district_id")
    private String districtId;

    @JsonProperty("district_name")
    private String districtName;
}
