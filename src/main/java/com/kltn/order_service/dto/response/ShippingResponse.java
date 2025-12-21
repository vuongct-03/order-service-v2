package com.kltn.order_service.dto.response;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShippingResponse {

    private Long orderId;

    private Long shipper;

    private String shippingAddress;

    private Date deliveryDate;

    private String image1;

    private String image2;

    private String image3;

    private String image4;

    private String status;
}
