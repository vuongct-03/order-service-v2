package com.kltn.order_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kltn.order_service.dto.request.CartItemRequestDTO;
import com.kltn.order_service.dto.response.CartItemResponse;
import com.kltn.order_service.dto.response.ResponseData;
import com.kltn.order_service.dto.response.ResponseError;
import com.kltn.order_service.service.CartItemService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/")
    public ResponseData<?> saveCartItem(@RequestBody CartItemRequestDTO request) {
        try {
            Long id = cartItemService.saveCateItem(request);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "add item successful!", id);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/increase/{id}")
    public ResponseData<?> increaseQuantity(@PathVariable Long id) {
        try {
            cartItemService.increaseQuantity(id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "increase item successful!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/decrease/{id}")
    public ResponseData<?> decreaseQuantity(@PathVariable Long id) {
        try {
            cartItemService.decreaseQuantity(id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "decrease quantity item successful!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseData<?> getItems() {
        try {
            List<CartItemResponse> items = cartItemService.getCartItems();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get item successful!", items);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get item failure");
        }
    }

    @GetMapping("/{userId}")
    public ResponseData<?> getItemsByUser(@PathVariable Long userId) {
        try {
            List<CartItemResponse> items = cartItemService.getCartItemsByUser(userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get item successful!", items);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getCause().getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseData<?> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        try {
            cartItemService.updateQuantity(id, quantity);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "update quantity successful!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteQuantity(@PathVariable Long id) {
        try {
            cartItemService.deleteItem(id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "delete quantity successful!");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
