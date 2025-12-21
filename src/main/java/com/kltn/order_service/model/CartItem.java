package com.kltn.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_cart_item")
public class CartItem extends AbstractEntity {

    @Column(name = "spec_id")
    private String specId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "name")
    private String name;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_avatar")
    private String productAvatar;

    @Column(name = "price")
    private double price;

    @Column(name = "discount_percent")
    private double discountPercent;

}
