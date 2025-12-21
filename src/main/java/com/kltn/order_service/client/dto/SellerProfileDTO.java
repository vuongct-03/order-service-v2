package com.kltn.order_service.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellerProfileDTO {
    
    private Long sellerId;

    private String bankAccount;

    private String bankName;

    private String beneficiaryName;

    private Double balance;

    private Double revenue;

    private String referralCode;

    private double commissionRate;
}
