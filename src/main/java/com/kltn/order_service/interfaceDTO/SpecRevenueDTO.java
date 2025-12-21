package com.kltn.order_service.interfaceDTO;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public interface SpecRevenueDTO {
    String getSpecId();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date getDate();

    double getTotalPrice();

    int getTotalItem();
}
