package com.kltn.order_service.controller;

import org.springframework.web.bind.annotation.*;

import com.kltn.order_service.service.ProvinceService;

@RestController
@RequestMapping(path = "/api/province")
public class ProvinceController {

    @GetMapping("/getprovince")
    public Object getProvince() {
        return ProvinceService.fetchAllProvinces();
    }

    @GetMapping("/getdistricts")
    public Object getAllDistricts(@RequestParam String provinceId) {
        return ProvinceService.fetchAllDistrictByProvince(provinceId);
    }

    @GetMapping("/getward")
    public Object getAllWard(@RequestParam String destrictId) {
        return ProvinceService.fetchAllWardByDistrict(destrictId);
    }
}
