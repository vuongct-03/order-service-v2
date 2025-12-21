package com.kltn.order_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kltn.order_service.dto.response.ResponseData;
import com.kltn.order_service.dto.response.ResponseError;
import com.kltn.order_service.service.StatisticsService;

@RestController
@RequestMapping(path = "/api/statistic")
public class StatisticController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/revenue-per-day")
    public ResponseData<?> getRevenuePerDay() {
        try {
            Map<String, Double> res = statisticsService.getRevenuePerDay();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Revenue per day!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/revenue-per-week")
    public ResponseData<?> getRevenuePerWeek() {
        try {
            Map<String, Double> res = statisticsService.getRevenuePerWeek();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Revenue per week!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/revenue-per-month")
    public ResponseData<?> getRevenuePerMonth() {
        try {
            Map<String, Double> res = statisticsService.getRevenuePerMonth();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Revenue per day!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/revenue-per-quanter")
    public ResponseData<?> getRevenuePerQuater() {
        try {
            Map<String, Double> res = statisticsService.getRevenuePerQuater();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Revenue per Quanter!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/revenue-per-Year")
    public ResponseData<?> getRevenuePerYear() {
        try {
            Map<String, Double> res = statisticsService.getRevenuePerYear();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Revenue per day!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/count-order-by-status")
    public ResponseData<?> getOrderCountByStatus() {
        try {
            Map<String, Long> res = statisticsService.getOrderCountByStatus();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "count-order-by-statu!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/best-selling-product")
    public ResponseData<?> getBestSellingProducts() {
        try {
            List<Map<String, Object>> res = statisticsService.getBestSellingProducts();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "best-selling-product!", res);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }
}
