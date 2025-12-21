package com.kltn.order_service.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.kltn.order_service.repository.OrderItemRepository;
import com.kltn.order_service.repository.OrderRepository;
import com.kltn.order_service.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Double> getRevenuePerDay() {
        List<Object[]> rows = orderRepository.getRevenuePerDay();
        return rows.stream().collect(Collectors.toMap(
            row -> row[0].toString(),
            row -> (Double) row[1]
        ));
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Double> getRevenuePerWeek() {
        List<Object[]> rows = orderRepository.getRevenuePerWeek();
        return rows.stream().collect(Collectors.toMap(
            row -> {
                String weekStr = row[0].toString(); // 202534
                String year = weekStr.substring(0, 4);
                String week = weekStr.substring(4);
                return year + "-W" + week;          // "2025-W34"
            },
            row -> ((Number) row[1]).doubleValue(),
            (oldValue, newValue) -> oldValue,
            LinkedHashMap::new
        ));
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Double> getRevenuePerMonth() {
        List<Object[]> rows = orderRepository.getRevenuePerMonth();
        return rows.stream().collect(Collectors.toMap(
            row -> row[0].toString() + "-" + String.format("%02d", row[1]), // "2025-01"
            row -> ((Number) row[2]).doubleValue(),
            (oldValue, newValue) -> oldValue, // tránh lỗi duplicate key
            LinkedHashMap::new // giữ nguyên thứ tự ASC
        ));
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Double> getRevenuePerQuater() {
        List<Object[]> rows = orderRepository.getRevenuePerQuarter();
        return rows.stream().collect(Collectors.toMap(
            row -> row[0].toString() + "-" + String.format("%02d", row[1]), // "2025-01"
            row -> ((Number) row[2]).doubleValue(),
            (oldValue, newValue) -> oldValue, // tránh lỗi duplicate key
            LinkedHashMap::new // giữ nguyên thứ tự ASC
        ));
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Double> getRevenuePerYear() {
        List<Object[]> rows = orderRepository.getRevenuePerYear();
        return rows.stream().collect(Collectors.toMap(
            row -> row[0].toString(),
            row -> (Double) row[1]
        ));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public Map<String, Long> getOrderCountByStatus() {
        List<Object[]> rows = orderRepository.countOrdersByStatus();
        return rows.stream().collect(Collectors.toMap(
            row -> (String) row[0],
            row -> (Long) row[1]
        ));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public List<Map<String, Object>> getBestSellingProducts() {
        List<Object[]> rows = orderItemRepository.getBestSellingProducts();
        return rows.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("specId", r[0]);
            map.put("specName", r[1]);
            map.put("totalSold", r[2]);
            return map;
        }).collect(Collectors.toList());
    }

    
}
