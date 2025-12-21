package com.kltn.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WardDTO {
    @JsonProperty("ward_id")
    private String wardId;

    @JsonProperty("ward_name")
    private String wardName;
}
