package com.kltn.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.kltn.order_service.client.dto.SpecificationResponse;
import com.kltn.order_service.config.FeignClientConfig;

@FeignClient(name = "spec-service", url = "${product_service.api.url}", configuration = FeignClientConfig.class)
public interface SpecificationClient {
    @GetMapping("/spec/{id}")
    SpecificationResponse getSpecificationById(@PathVariable String id);

    @PutMapping("/spec/{id}/{quantity}")
    boolean checkAndDecreaseStock(@PathVariable String id, @PathVariable int quantity);
}
