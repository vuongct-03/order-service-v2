package com.kltn.order_service.client.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBehaviorDTO {

    @NotNull(message = "user id not null")
    private Long userId;

    @NotNull(message = "user id not null")
    private String specId;

    private String action;
}

