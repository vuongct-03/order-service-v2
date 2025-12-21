package com.kltn.order_service.dto.response;

import com.kltn.order_service.util.PeriodType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyticsSumaryResponse {
    private PeriodType periodType;

    private String periodValue;

    private double totalRevenue;

    private double totalOrder;

    private double totalProductSold;

    @Getter
    @Builder
    public static class AnalyticByMonth {
        private int month;
        private int year;

        private double totalRevenue;

        private double totalOrder;

        private double totalProductSold;

    }

}
