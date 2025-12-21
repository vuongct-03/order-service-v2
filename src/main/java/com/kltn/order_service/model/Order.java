package com.kltn.order_service.model;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_order")
public class Order extends AbstractEntity {

    // @Column(name = "order_code", unique = true, nullable = false, updatable = false)
    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "user_id")
    @NotNull(message = "user id not null")
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    @Builder.Default
    private Set<OrderItem> orderItems = new HashSet<>();

    // điểm giao hàng là: địa chỉ + đường (id đường) + phường (id phường) + thành
    // phố (id thành phố)
    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_fee")
    private int shippingFee;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "trans_type")
    private String transType;

    @Column(name = "expected_delivery_time")
    private String expectedDeliveryTime;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "delivered_image_url")
    private List<String> deliveredImageURL;

    @Column(name = "is_payment")
    private boolean isPayment;

    @Column(name = "referral_code")
    private String referralCode;

    // @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private String status;

}
