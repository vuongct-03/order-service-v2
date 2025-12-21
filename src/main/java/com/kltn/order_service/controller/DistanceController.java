package com.kltn.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kltn.order_service.dto.response.ResponseData;
import com.kltn.order_service.service.DistanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/distance")
public class DistanceController {
    @Autowired
    private DistanceService distanceService;

    @GetMapping("/")
    public ResponseData<?> getDisTance(@RequestParam String origin, @RequestParam String destination) {
        return new ResponseData<>(0, "ok", distanceService.getDistance(origin, destination));
    }

    @GetMapping("/Coordinates")
    public ResponseData<?> getCoordinates(@RequestParam String address) {
        return new ResponseData<>(0, "ok", distanceService.getCoordinates(address));
    }

}
