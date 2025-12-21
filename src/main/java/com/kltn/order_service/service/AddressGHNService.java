package com.kltn.order_service.service;

import com.kltn.order_service.dto.GHN.address.DistrictGHNResponseWrapper;
import com.kltn.order_service.dto.GHN.address.DistrictGhnDTO;
import com.kltn.order_service.dto.GHN.address.ProvinceGHNResponseWrapper;
import com.kltn.order_service.dto.GHN.address.ProvinceGhnDTO;
import com.kltn.order_service.dto.GHN.address.WardGHNDTO;
import com.kltn.order_service.dto.GHN.address.WardGHNResponseWrapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AddressGHNService {

    private static final String provinceAPI = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private static final String districtAPI = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String WARD_API = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    private static final String token = "4eea021f-5d7d-11f0-9b81-222185cb68c8";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ProvinceGhnDTO> fetchAllProvinces() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Token", token); // BẮT BUỘC
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ProvinceGHNResponseWrapper> response = restTemplate.exchange(
                    provinceAPI,
                    HttpMethod.GET,
                    entity,
                    ProvinceGHNResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData(); // Sửa đúng key
            } else {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to fetch provinces");
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi gọi GHN API: " + e.getMessage());
        }
    }

    public List<DistrictGhnDTO> fetchAllDistrictsByProvince(int provinceId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Token", token);

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<DistrictGHNResponseWrapper> response = restTemplate.exchange(
                    districtAPI + "?province_id=" + provinceId, // =
                                                                // "https://online-gateway.ghn.vn/shiip/public-api/master-data/district"
                    HttpMethod.GET,
                    entity,
                    DistrictGHNResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            } else {
                throw new ResponseStatusException(response.getStatusCode(), "Lấy danh sách quận/huyện thất bại");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi gọi GHN API: " + e.getMessage());
        }
    }

    public List<WardGHNDTO> fetchAllWardsByDistrict(int districtId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Token", token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = WARD_API + "?district_id=" + districtId;
            ResponseEntity<WardGHNResponseWrapper> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    WardGHNResponseWrapper.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData();
            } else {
                throw new ResponseStatusException(
                        response.getStatusCode(),
                        "Không thể lấy danh sách phường từ GHN");
            }

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi gọi API GHN: " + e.getMessage());
        }
    }
}
