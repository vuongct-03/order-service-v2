package com.kltn.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kltn.order_service.model.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);

    CartItem findBySpecIdAndUserId(String specId, Long userId);

    CartItem findBySpecId(String specId);

    void deleteAllByUserId(Long userId);
    // @Query(value = "SELECT * FROM tbl_cart_item where spec_id = :specId AND
    // user_id = :userId", nativeQuery = true)
    // CartItem getProductCartsByUserIdAndSpecId(@Param("specId") Long specId,
    // @Param("userId") String userId);

}
