package com.kltn.order_service.client.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.kltn.order_service.client.SpecificationClient;
import com.kltn.order_service.client.dto.SpecificationDTO;
import com.kltn.order_service.client.dto.SpecificationResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecificationClientService {
    private final SpecificationClient specificationClient;

    // @PreAuthorize("hasAnyRole('OWNER', 'STAFF', 'ADMIN')")
    // @RateLimiter(name = "productServiceRL", fallbackMethod = "fallbackGetSpecificationRL")
    // public SpecificationDTO getSpecification(String specId) {
    //     return getSpecificationWithCB(specId);
    // }

    // @CircuitBreaker(name="productServiceCB", fallbackMethod = "fallbackGetSpecificationCB")
    // public SpecificationDTO getSpecificationWithCB(String specId) {
    //     SpecificationResponse response = specificationClient.getSpecificationById(specId);
    //     return response.getData();
    // }


    @PreAuthorize("hasAnyRole('OWNER', 'STAFF', 'ADMIN')")
    public SpecificationDTO getSpecificationById(String specId) {
        SpecificationResponse response = specificationClient.getSpecificationById(specId);
        return response.getData();
    }

    public boolean checkAndDecreaseStock(String specId, int quantity) {
        boolean response = specificationClient.checkAndDecreaseStock(specId, quantity);
        return response;
    }

    // Fallback method ném exception để báo lỗi
    // public SpecificationDTO fallbackGetSpecificationCB(String specId, Throwable ex) {
    //     throw new RuntimeException("Product Service temporarily unavailable");
    // }

    // public SpecificationDTO fallbackGetSpecificationRL(String specId, Throwable ex) {
    //     throw new RuntimeException("Service temporarily too many requests");
    // }
}


