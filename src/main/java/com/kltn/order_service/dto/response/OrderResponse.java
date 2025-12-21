package com.kltn.order_service.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse implements Serializable {

    private Long id;
    private String fullName;
    private String phone;
    private String orderCode;
    private Long userId;
    private String shippingAddress;
    private double totalPrice;
    private double shippingFee;
    private Date orderDate;
    private String status;
    private boolean isPayment;
    private String referralCode;
    private List<String> deliveredImageURL;

    private String expectedDeliveryTime;
    private String transType;
    

    private List<OrderItemResponse> items;
}
