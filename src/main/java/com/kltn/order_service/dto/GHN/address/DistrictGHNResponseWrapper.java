package com.kltn.order_service.dto.GHN.address;

import lombok.Data;
import java.util.List;

@Data
public class DistrictGHNResponseWrapper {
    private int code;
    private String message;
    private List<DistrictGhnDTO> data;
}
