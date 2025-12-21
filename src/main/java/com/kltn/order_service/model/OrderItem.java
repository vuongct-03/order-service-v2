package com.kltn.order_service.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order_item")
public class OrderItem extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "spec_id")
    private String specId;

    private String specName;
    private int quantity;
    private double price; // Giá tại thời điểm đặt hàng
}
