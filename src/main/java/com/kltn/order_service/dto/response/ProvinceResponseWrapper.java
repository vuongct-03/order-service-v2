package com.kltn.order_service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProvinceResponseWrapper {
    private List<ProvinceDTO> results;
}
