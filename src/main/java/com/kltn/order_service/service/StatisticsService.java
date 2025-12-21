package com.kltn.order_service.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Map<String, Double> getRevenuePerDay();

    Map<String, Double> getRevenuePerWeek();

    Map<String, Double> getRevenuePerMonth();

    Map<String, Double> getRevenuePerQuater();

    Map<String, Double> getRevenuePerYear();

    Map<String, Long> getOrderCountByStatus();

    List<Map<String, Object>> getBestSellingProducts();
}
