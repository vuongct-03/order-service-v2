package com.kltn.order_service.dto.request;

import lombok.Builder;
import lombok.Getter;

public class ShippingDTO {

    @Builder
    @Getter
    public static class ShippingRequestDTO {
        private Long orderId;
        private Long shipper;
        private String shippingAddress;
    }

    @Builder
    @Getter
    public static class ShippingUpdateDTO {
        private String image1;
        private String image2;
        private String image3;
        private String image4;
    }

}
