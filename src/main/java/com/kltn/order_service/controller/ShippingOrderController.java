package com.kltn.order_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kltn.order_service.dto.GHN.CancelOrderRequest;
import com.kltn.order_service.dto.GHN.ShippingOrderRequest;
import com.kltn.order_service.service.ShippingOrderService;

@RestController
@RequestMapping(path = "/api/shipping-order")
public class ShippingOrderController {

    @Autowired
    private ShippingOrderService shippingOrderService;

    @PostMapping("/create")
    public Object createOrderGHN(@RequestBody ShippingOrderRequest request) {
        return shippingOrderService.createOrderGHN(request);
    }

    @PostMapping("/cancel")
    public Object cancelOrderGHN(@RequestBody CancelOrderRequest orderCode) {
        return shippingOrderService.cancelOrderGHN(orderCode);
    }
}
