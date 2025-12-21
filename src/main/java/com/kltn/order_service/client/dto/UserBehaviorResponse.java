package com.kltn.order_service.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBehaviorResponse {
    private int status;
    private String message;
    private UserBehaviorDTO data;
}
