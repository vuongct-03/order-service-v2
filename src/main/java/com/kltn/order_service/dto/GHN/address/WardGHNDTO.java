package com.kltn.order_service.dto.GHN.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardGHNDTO {

    @JsonProperty("WardCode")
    private String wardCode;

    @JsonProperty("DistrictID")
    private int districtId;

    @JsonProperty("WardName")
    private String wardName;

}
