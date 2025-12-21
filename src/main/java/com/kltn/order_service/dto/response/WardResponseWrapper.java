package com.kltn.order_service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WardResponseWrapper {
    private List<WardDTO> results;
}
