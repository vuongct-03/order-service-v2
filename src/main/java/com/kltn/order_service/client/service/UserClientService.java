package com.kltn.order_service.client.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.kltn.order_service.client.UserClient;
import com.kltn.order_service.client.dto.SellerProfileDTO;
import com.kltn.order_service.client.dto.SellerProfileResponse;
import com.kltn.order_service.client.dto.UserBehaviorDTO;
import com.kltn.order_service.client.dto.UserDTO;
import com.kltn.order_service.client.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientService {
    private final UserClient userClient;

    @PreAuthorize("hasAnyRole('OWNER', 'STAFF', 'ADMIN', 'SELLER')")
    // @CircuitBreaker(name="userServiceCB", fallbackMethod = "fallbackGetUser")
    public UserDTO getUserById(Long userId) {
        UserResponse response = userClient.getUserById(userId);
        return response.getData(); // Lấy phần "data" từ JSON trả về
    }


    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN', 'SELLER')")
    public SellerProfileDTO getSellerProfileByCode(String code) {
        SellerProfileResponse response = userClient.getSellerProfileByCode(code);
        return response.getData(); // Lấy phần "data" từ JSON trả về
    }

    @Async
    public void saveUserBehavior(UserBehaviorDTO request) {
        userClient.saveUserBehavior(request);
    }
}
