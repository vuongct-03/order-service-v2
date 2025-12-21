package com.kltn.order_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kltn.order_service.interfaceDTO.SpecRevenueDTO;
import com.kltn.order_service.model.Order;
import com.kltn.order_service.util.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        List<Order> findByCreateAtBetween(Date start, Date end);

        @Query(value = "SELECT \r\n" + //
                "    spec_id AS specId, \r\n" + //
                "    DATE(create_at) AS date,\r\n" + //
                "    SUM(quantity * price) AS totalPrice, \r\n" + //
                "    SUM(quantity) AS totalItem\r\n" + //
                "FROM tbl_order_item \r\n" + //
                "GROUP BY spec_id, DATE(create_at)" + //
                "", nativeQuery = true)
        List<SpecRevenueDTO> getTotalRevenuePerProduct();

        List<Order> findAllByStatusNot(String status);

        List<Order> findByStatus(String status);

        Order findByOrderCode(String orderCode);

        List<Order> findOrderByUserId(Long userId);

        List<Order> findByReferralCode(String referralCode);


        // 1. Doanh thu theo ngày
        @Query("SELECT FUNCTION('DATE', o.createAt), SUM(o.totalPrice) " +
                "FROM Order o " +
                "GROUP BY FUNCTION('DATE', o.createAt) " +
                "ORDER BY FUNCTION('DATE', o.createAt) ASC")
        List<Object[]> getRevenuePerDay();

        // Doanh thu theo tuần
        @Query(value = "SELECT YEARWEEK(o.create_at, 1) AS week, SUM(o.total_price) " +
                "FROM tbl_order o " +
                "GROUP BY YEARWEEK(o.create_at, 1) " +
                "ORDER BY week ASC", nativeQuery = true)
        List<Object[]> getRevenuePerWeek();

        // Doanh thu theo tháng
        @Query(value = "SELECT YEAR(o.create_at) AS year, MONTH(o.create_at) AS month, SUM(o.total_price) " +
                "FROM tbl_order o " +
                "GROUP BY YEAR(o.create_at), MONTH(o.create_at) " +
                "ORDER BY year ASC, month ASC", nativeQuery = true)
        List<Object[]> getRevenuePerMonth();

        @Query(value = "SELECT YEAR(o.create_at) AS year, QUARTER(o.create_at) AS quarter, SUM(o.total_price) " +
                "FROM tbl_order o " +
                "GROUP BY YEAR(o.create_at), QUARTER(o.create_at) " +
                "ORDER BY year ASC, quarter ASC", nativeQuery = true)
        List<Object[]> getRevenuePerQuarter();

        // Doanh thu theo năm
        @Query(value = "SELECT YEAR(o.create_at) AS year, SUM(o.total_price) " +
                        "FROM tbl_order o " +

                        "GROUP BY YEAR(o.create_at) " +
                        "ORDER BY year ASC", nativeQuery = true)
        List<Object[]> getRevenuePerYear();

        // 2. Số lượng đơn theo trạng thái
        @Query("SELECT o.status, COUNT(o) " +
                "FROM Order o " +
                "GROUP BY o.status")
        List<Object[]> countOrdersByStatus();


    // 3. Doanh thu theo khách hàng
    // @Query("SELECT o.userId, SUM(o.totalPrice) " +
    //         "FROM Order o " +
    //         "WHERE o.status = 'DELIVERED' " +
    //         "GROUP BY o.userId")
    // List<Object[]> getRevenueByUser();

}
