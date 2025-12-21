package com.kltn.order_service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PeriodType {
    @JsonProperty("daily")
    DAILY,

    @JsonProperty("monthly")
    MONTHLY,

    @JsonProperty("quanterly")
    QUANTERLY,

    @JsonProperty("yearly")
    YEARLY;
}
