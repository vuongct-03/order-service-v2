package com.kltn.order_service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kltn.order_service.client.dto.SpecificationDTO;
import com.kltn.order_service.client.dto.UserDTO;
import com.kltn.order_service.client.service.SpecificationClientService;
import com.kltn.order_service.client.service.UserClientService;
import com.kltn.order_service.dto.request.CartItemRequestDTO;
import com.kltn.order_service.dto.response.CartItemResponse;
import com.kltn.order_service.kafka.dto.ProductResponseKafka;
import com.kltn.order_service.kafka.dto.SpecificationResponseKafka;
import com.kltn.order_service.model.CartItem;
import com.kltn.order_service.repository.CartItemRepository;
import com.kltn.order_service.service.CartItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private UserClientService userClientService;

    @Autowired
    private SpecificationClientService specificationClientService;

    @Autowired
    private CartItemRepository cartItemRepository;


    private CartItem getCateItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found cart item"));
        return cartItem;
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public Long saveCateItem(CartItemRequestDTO request) {

        UserDTO user = userClientService.getUserById(request.getUserId());

        SpecificationDTO spec = specificationClientService.getSpecificationById(request.getSpecId());


        CartItem cartItemExist = cartItemRepository.findBySpecIdAndUserId(request.getSpecId(), request.getUserId());

        if(request.getQuantity() > spec.getQuantity()){
            throw new RuntimeException("Sản phẩm " + spec.getName() + " chỉ còn " + spec.getQuantity() + "sản phẩm");
        }

        CartItem cartItem;
        if (cartItemExist != null) {

            int newQuantity = cartItemExist.getQuantity() + request.getQuantity();
            if(newQuantity > spec.getQuantity()){
                throw new RuntimeException("Sản phẩm " + spec.getName() + " không đủ số lượng!");
            }
            cartItemExist.setQuantity(newQuantity);
            cartItemRepository.save(cartItemExist);
            return cartItemExist.getId();
        } else {
            CartItem cateItem = CartItem.builder()
                    .userId(user.getId())
                    .specId(spec.getId())
                    .quantity(request.getQuantity())
                    .productId(spec.getProduct().getId())
                    .productAvatar(spec.getImageURLs().get(0))
                    .name(spec.getName())
                    .price(spec.getPrice())
                    .discountPercent(spec.getProduct().getDiscountPercent())
                    .build();
            
                     // Save user behavior

            cartItem = cartItemRepository.save(cateItem);
        }
    
        return cartItem.getId();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public void updateQuantity(Long id, int quantity) {
        try {
            CartItem cartItem = getCateItemById(id);

            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public void deleteItem(Long id) {
        try {

            cartItemRepository.deleteById(id);


        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public List<CartItemResponse> getCartItems() {
        try {
            List<CartItem> items = cartItemRepository.findAll();

            List<CartItemResponse> responseItems = items.stream().map(
                    item -> CartItemResponse.builder()
                            .id(item.getId())
                            .specificationId(item.getSpecId())
                            .userId(item.getUserId())
                            .quantity(item.getQuantity())
                            .build())
                    .toList();

            return responseItems;
        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public List<CartItemResponse> getCartItemsByUser(Long userId) {
        try {
            List<CartItem> items = cartItemRepository.findByUserId(userId);

            List<CartItemResponse> responseItems = items.stream().map(
                    item -> {
                        try {
                            return CartItemResponse.builder()
                                    .id(item.getId())
                                    .specificationId(item.getSpecId())
                                    .userId(item.getUserId())
                                    .productName(item.getName())
                                    .productId(item.getProductId())
                                    .productAvatar(item.getProductAvatar())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .discountPercent(item.getDiscountPercent())
                                    .build();
                        } catch (Exception e) {
                            throw new RuntimeException("Lỗi khi lấy thông tin sản phẩm cho specId: " + item.getSpecId(),
                                    e);
                        }
                    }).toList();

            return responseItems;
        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    public void updateCartItemKafka(SpecificationResponseKafka spec) {
        try{
            CartItem cartItem = cartItemRepository.findBySpecId(spec.getId());

            cartItem.setName(spec.getName());
            cartItem.setPrice(spec.getPrice());
            cartItem.setProductAvatar(spec.getImageURLs().get(0));

            cartItemRepository.save(cartItem);
            
        }catch(Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

        @Override
    public void updateProductForCartItemKafka(ProductResponseKafka product) {
        try{
            CartItem cartItem = cartItemRepository.findByProductId(product.getId());
            cartItem.setName(product.getName());
            cartItem.setDiscountPercent(product.getDiscountPercent());

            cartItemRepository.save(cartItem);
            
        }catch(Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public void increaseQuantity(Long id) {
        try{
            CartItem item = getCateItemById(id);
            item.setQuantity(item.getQuantity() + 1);
            
            cartItemRepository.save(item);
        }catch(Exception e){
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public void decreaseQuantity(Long id) {
        try{
            CartItem item = getCateItemById(id);
            item.setQuantity(item.getQuantity() - 1);
            
            cartItemRepository.save(item);
        }catch(Exception e){
            throw new RuntimeException("lỗi", e);
        }
    }

}
