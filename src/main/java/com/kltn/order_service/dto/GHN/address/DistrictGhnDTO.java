package com.kltn.order_service.dto.GHN.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DistrictGhnDTO {

    @JsonProperty("DistrictID")
    private int districtId;

    @JsonProperty("ProvinceID")
    private int provinceId;

    @JsonProperty("DistrictName")
    private String districtName;
}