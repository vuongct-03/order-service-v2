package com.kltn.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kltn.order_service.dto.request.OrderRequestDTO;
import com.kltn.order_service.dto.response.OrderResponse;
import com.kltn.order_service.dto.response.ResponseData;
import com.kltn.order_service.dto.response.ResponseError;
import com.kltn.order_service.service.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/auth")
    public Object debugAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities(); // in ra roles hiện tại
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("/test-seller")
    public String testSeller(Authentication authentication) {
        return "Authorities: " + authentication.getAuthorities();
    }
    
    @PostMapping("/")
    public ResponseData<?> createOrder(@RequestBody OrderRequestDTO request) {
        try {
            Long id = orderService.createOrder(request);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "add order successful!", id);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/update-order-payment/{id}")
    public ResponseData<?> updateOrderPayment(@PathVariable Long id) {
        try {
            String message = orderService.updateOrderPayment(id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "successful!", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/shipping/{orderId}/{shipperId}")
    public ResponseData<?> shipping(@PathVariable Long orderId, @PathVariable Long shipperId) {
        try {
            orderService.shippingOrder(orderId, shipperId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "add order successful!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/delivered/{orderId}")
    public ResponseData<?> deliveredOrder(@PathVariable Long orderId, @RequestParam("files") List<MultipartFile> images,
            @RequestParam("folder") String folder) {
        try {
            orderService.deliveredOrder(orderId, images, folder);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "delivered order!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseData<?> getOrder(@PathVariable Long id) {
        try {
            OrderResponse order = orderService.getOrder(id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "add order successful!", order);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/")
    public ResponseData<?> getAllOrder() {
        try {
            List<OrderResponse> response = orderService.getAllOrders();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "get all order successful!", response);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseData<?> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrderResponse> response = orderService.getAllOrdersByStatus(status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "get all order successful!", response);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseData<?> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<OrderResponse> response = orderService.getOrdersByUser(userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "get order by user successful!", response);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @GetMapping("/referral-code/{code}")
    public ResponseData<?> getOrdersByReferralCode(@PathVariable String code) {
        try {
            List<OrderResponse> response = orderService.getOrdersByReferralCode(code);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "get order by referral code successful!", response);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/cancel/{orderId}/{orderCode}")
    public ResponseData<?> cancelOrder(@PathVariable Long orderId, @PathVariable String orderCode) {
        try {
            String message = orderService.cancelOrder(orderId, orderCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/delivering/{orderId}")
    public ResponseData<?> deliveringOrder(@PathVariable Long orderId) {
        try {
            String message = orderService.deliveringOrder(orderId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

}
