package com.kltn.order_service.dto.GHN.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProvinceGhnDTO {
    @JsonProperty("ProvinceID")
    private int provinceId;

    @JsonProperty("ProvinceName")
    private String provinceName;

    @JsonProperty("Code")
    private String code;
}
