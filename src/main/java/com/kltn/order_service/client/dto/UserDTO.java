package com.kltn.order_service.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
