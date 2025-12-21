package com.kltn.order_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.kltn.order_service.dto.GHN.CancelOrderRequest;
import com.kltn.order_service.dto.GHN.GHNCreateOrderResponse;
import com.kltn.order_service.dto.GHN.ShippingOrderRequest;
import com.kltn.order_service.model.Order;
import com.kltn.order_service.repository.OrderRepository;

@Service
public class ShippingOrderService {
    private static String createOrderGHNAPI = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
    private static String cancelOrderGHNAPI = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel";
    private static final String token = "4eea021f-5d7d-11f0-9b81-222185cb68c8";
    private static final int shopId = 197091;
    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private OrderRepository orderRepository;

    private void updateOrderFromGHN(Long id, String orderCode, String expectedDeliveryTime, String transType, int shippingFee) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderCode(orderCode);
        order.setExpectedDeliveryTime(expectedDeliveryTime);
        order.setTransType(transType);
        order.setShippingFee(shippingFee);

        orderRepository.save(order);
    }

    public GHNCreateOrderResponse createOrderGHN(ShippingOrderRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set("ShopId", String.valueOf(shopId)); // Ví dụ: "5883297"
            headers.set("Token", token); // Token hợp lệ

            HttpEntity<ShippingOrderRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<GHNCreateOrderResponse> response = restTemplate.exchange(
                    createOrderGHNAPI, // URL ví dụ:
                    // "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create"
                    HttpMethod.POST,
                    entity,
                    GHNCreateOrderResponse.class);

            GHNCreateOrderResponse body = response.getBody();
            if (body == null) {
                throw new IllegalStateException("Phản hồi GHN không hợp lệ.");
            }

            GHNCreateOrderResponse.GHNData data = body.getData();

            

            GHNCreateOrderResponse soResponse = GHNCreateOrderResponse.builder()
                    .message("Tạo đơn hàng thành công. Mã đơn hàng: " + data.getOrderCode())
                    .data(data).build();

            
            updateOrderFromGHN(request.getOrderId(), data.getOrderCode(), data.getExpectedDeliveryTime(), data.getTransType(), data.getTotalFee());

            return soResponse; // Có thể đổi Object thành GHNOrderResponseWrapper nếu muốn lấy dữ liệu chuẩn

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("GHN lỗi (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi không xác định: " + e.getMessage());
        }
    }

    public Object cancelOrderGHN(CancelOrderRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set("ShopId", String.valueOf(shopId)); // Ví dụ: "5883297"
            headers.set("Token", token); // Token hợp lệ

            HttpEntity<CancelOrderRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    cancelOrderGHNAPI, // URL ví dụ:
                    // "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create"
                    HttpMethod.POST,
                    entity,
                    Object.class);

            List<String> orderCodes = request.getOrderCodes();
            orderCodes.forEach(orderCode -> {
                Order orderCancel = orderRepository.findByOrderCode(orderCode);
                if (orderCancel != null) {
                    orderCancel.setStatus("cancel");
                    orderRepository.save(orderCancel);
                }
            });

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("GHN lỗi (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi không xác định: " + e.getMessage());
        }
    }

    public String getOrderStatusFromGHN(String orderCode) {
        String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("order_code", orderCode);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody.getJSONObject("data").getString("status"); // trạng thái: ready_to_pick, delivering...
        }
        return null;
    }

    @Scheduled(fixedRate = 10 * 60 * 1000) // mỗi 10 phút
    public void updateOrderStatuses() {
        List<Order> orders = orderRepository.findAllByStatusNot("delivered"); // ví dụ chỉ cập nhật đơn chưa giao

        for (Order order : orders) {
            try {
                String status = getOrderStatusFromGHN(order.getOrderCode());
                if (status != null && !status.equals(order.getStatus())) {
                    order.setStatus(status);
                    orderRepository.save(order);
                    System.out.println("✅ Updated order " + order.getOrderCode() + " to status: " + status);
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to update order: " + order.getOrderCode());
            }
        }
    }

}
