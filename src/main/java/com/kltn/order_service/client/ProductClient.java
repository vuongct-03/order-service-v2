package com.kltn.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kltn.order_service.client.dto.ProductResponse;

@FeignClient(name = "product-service", url = "${product_service.api.url}")
public interface ProductClient {
    @GetMapping("/product/{id}")
    ProductResponse getProductById(@PathVariable String id);
}
