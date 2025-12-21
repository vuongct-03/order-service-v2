package com.kltn.order_service.service;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.kltn.order_service.dto.response.DistrictDTO;
import com.kltn.order_service.dto.response.DistrictResponseWrapper;
import com.kltn.order_service.dto.response.ProvinceDTO;
import com.kltn.order_service.dto.response.ProvinceResponseWrapper;
import com.kltn.order_service.dto.response.WardDTO;
import com.kltn.order_service.dto.response.WardResponseWrapper;

public class ProvinceService {
    private static String provinceAPI = "https://vapi.vnappmob.com/api/v2/province/";
    private static String districtsAPI = "https://vapi.vnappmob.com/api/v2/province/district/";
    private static String wardAPI = "https://vapi.vnappmob.com/api/v2/province/ward/";

    private static RestTemplate restTemplate = new RestTemplate();
    // private static String Host = "https://vapi.vnappmob.com";

    public static List<ProvinceDTO> fetchAllProvinces() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Host", provinceAPI);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ProvinceResponseWrapper> response = restTemplate.exchange(
                    provinceAPI,
                    HttpMethod.GET,
                    entity,
                    ProvinceResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getResults();
            } else {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to fetch provinces");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static List<DistrictDTO> fetchAllDistrictByProvince(String provinceId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Host", districtsAPI);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<DistrictResponseWrapper> response = restTemplate.exchange(
                    districtsAPI + provinceId, HttpMethod.GET,
                    entity,
                    DistrictResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getResults();
            } else {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to fetch provinces");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static List<WardDTO> fetchAllWardByDistrict(String DistrictId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Host", districtsAPI);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<WardResponseWrapper> response = restTemplate.exchange(
                    wardAPI + DistrictId, HttpMethod.GET,
                    entity,
                    WardResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getResults();
            } else {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to fetch provinces");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
