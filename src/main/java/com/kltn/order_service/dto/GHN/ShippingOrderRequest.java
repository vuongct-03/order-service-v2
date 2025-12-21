package com.kltn.order_service.dto.GHN;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class ShippingOrderRequest {
    private int payment_type_id;
    private String note;
    private String required_note;

    private String from_name;
    private String from_phone;
    private String from_address;
    private String from_ward_code;
    private int from_district_id;

    private Long orderId;

    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_ward_code;
    private int to_district_id;

    private int cod_amount;
    private String content;

    private int weight;
    private int length;
    private int width;
    private int height;

    private int insurance_value;
    private int service_id;
    private int service_type_id;
    private Item[] items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String name;
        private int quantity;
        private String code;
        private int price;
    }
}
