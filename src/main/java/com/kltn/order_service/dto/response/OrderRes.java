package com.kltn.order_service.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRes<T> implements Serializable {
    private Long id;
    private Long userId;
    private String orderCode;
    private double totalPrice;
    private double shippingFee;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private boolean isPayment;
    private String referralCode;
    private List<String> deliveredImageURL;
}
