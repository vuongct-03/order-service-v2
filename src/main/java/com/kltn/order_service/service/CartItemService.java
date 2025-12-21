package com.kltn.order_service.service;

import java.util.List;

import com.kltn.order_service.dto.request.CartItemRequestDTO;
import com.kltn.order_service.dto.response.CartItemResponse;
import com.kltn.order_service.kafka.dto.SpecificationResponseKafka;

public interface CartItemService {
    Long saveCateItem(CartItemRequestDTO request);

    void updateQuantity(Long id, int quantity);

    void increaseQuantity(Long id);

    void decreaseQuantity(Long id);

    void updateCartItemKafka(SpecificationResponseKafka spec);

    void deleteItem(Long id);

    List<CartItemResponse> getCartItems();

    List<CartItemResponse> getCartItemsByUser(Long userId);

}
