package com.kltn.order_service.model;

import com.kltn.order_service.util.PeriodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "analytics_sumary")
public class AnalyticsSumary extends AbstractEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private PeriodType periodType;

    @Column(name = "period_value")
    private String periodValue;

    @Column(name = "total_revenue")
    private double totalRevenue;

    @Column(name = "total_order")
    private int totalOrder;

    @Column(name = "total_item_sold")
    private double totalItemSold;

}
