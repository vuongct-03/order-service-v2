package com.kltn.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProvinceDTO {
    @JsonProperty("province_id")
    private String provinceId;

    @JsonProperty("province_name")
    private String provinceName;
}
