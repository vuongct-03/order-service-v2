package com.kltn.order_service.dto.GHN.address;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceGHNResponseWrapper {
    private int code;
    private String message;
    private List<ProvinceGhnDTO> data; // GHN trả về key "data", không phải "results"
}
