package com.kltn.order_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kltn.order_service.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCreateAtBetween(Date start, Date end);

    List<OrderItem> findByOrderId(Long orderId);

    // 4. Top sản phẩm bán chạy
    @Query("""
        SELECT i.specId, i.specName, SUM(i.quantity) AS totalSold
        FROM OrderItem i
        JOIN i.order o
        GROUP BY i.specId, i.specName
        ORDER BY totalSold DESC
        """)
    List<Object[]> getBestSellingProducts();


}
