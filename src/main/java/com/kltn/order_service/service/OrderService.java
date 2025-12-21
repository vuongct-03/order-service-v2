package com.kltn.order_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kltn.order_service.dto.request.OrderRequestDTO;
import com.kltn.order_service.dto.response.OrderResponse;
import com.kltn.order_service.dto.response.OrderItemResponse;
import com.kltn.order_service.dto.response.OrderRes;

public interface OrderService {
    Long createOrder(OrderRequestDTO request);

    void shippingOrder(Long orderId, Long shipperId);

    void deliveredOrder(Long orderId, List<MultipartFile> images, String folder);

    OrderResponse getOrder(Long id);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getAllOrdersByStatus(String status);

    List<OrderResponse> getOrdersByUser(Long userId);

    List<OrderResponse> getOrdersByReferralCode(String referralCode);

    List<OrderRes<Object>> getAllOrder();

    List<OrderItemResponse> getAllItem();

    String updateOrderPayment(Long id);

    String cancelOrder(Long id, String orderCode);

    String deliveringOrder(Long id);



}
