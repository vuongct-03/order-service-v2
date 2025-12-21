package com.kltn.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kltn.order_service.client.dto.SellerProfileResponse;
import com.kltn.order_service.client.dto.UserBehaviorDTO;
import com.kltn.order_service.client.dto.UserResponse;
import com.kltn.order_service.config.FeignClientConfig;

import jakarta.validation.Valid;

@FeignClient(name = "user-service", url = "${user_service.api.url}", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @GetMapping("/seller-profile/get-by-referral-code/{code}")
    SellerProfileResponse getSellerProfileByCode(@PathVariable String code);

    
    @PostMapping("/behavior/")
    void saveUserBehavior(@Valid @RequestBody UserBehaviorDTO request);

}

