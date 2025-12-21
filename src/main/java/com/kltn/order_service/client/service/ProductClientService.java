package com.kltn.order_service.client.service;

import org.springframework.stereotype.Service;

import com.kltn.order_service.client.ProductClient;
import com.kltn.order_service.client.dto.ProductDTO;
import com.kltn.order_service.client.dto.ProductResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClientService {
    private final ProductClient productClient;

    @CircuitBreaker(name="productServiceCB", fallbackMethod = "fallbackGetProduct")
    public ProductDTO getProduct(String productId) {
        ProductResponse response = productClient.getProductById(productId);
        return response.getData(); // Lấy phần "data" từ JSON trả về
    }

    // Fallback method ném exception để báo lỗi
    public ProductDTO fallbackGetProduct(String productId, Throwable ex) {
        throw new RuntimeException("Product Service temporarily unavailable");
    }
}
