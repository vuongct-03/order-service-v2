package com.kltn.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kltn.order_service.service.AddressGHNService;

@RestController
@RequestMapping(path = "/api/provinceGHN")
public class AddressGHNController {

    @Autowired
    private AddressGHNService addressGHNService;

    @GetMapping("/getprovince")
    public Object getProvince() {
        return addressGHNService.fetchAllProvinces();
    }

    @GetMapping("/getdistrict")
    public Object getDistrictsByProvince(@RequestParam("province_id") int provinceId) {
        return addressGHNService.fetchAllDistrictsByProvince(provinceId);
    }

    @GetMapping("/getwards")
    public ResponseEntity<?> getWardsByDistrict(@RequestParam("district_id") int districtId) {
        return ResponseEntity.ok(addressGHNService.fetchAllWardsByDistrict(districtId));
    }

}