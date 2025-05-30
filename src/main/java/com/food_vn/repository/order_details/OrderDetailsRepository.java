package com.food_vn.repository.order_details;

import com.food_vn.model.order_details.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrdersId(Long orderId);
    List<OrderDetail> findByOrdersIdAndProductId(Long orderId, Long productId);

    boolean existsByProductId(Long productId);

    @Query(value = "SELECT od.product_id AS productId, SUM(od.quantity) AS totalSold " +
           "FROM order_detail od " +
           "JOIN orders o ON o.id = od.orders_id " +
           "WHERE o.status = 4 " +
           "GROUP BY od.product_id " +
           "ORDER BY totalSold DESC " +
           "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findTopSellingProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT c.id AS categoryId, c.name AS categoryName, SUM(od.quantity) AS totalSold " +
           "FROM order_detail od " +
           "JOIN product p ON p.id = od.product_id " +
           "JOIN category c ON c.id = p.category_id " +
           "JOIN orders o ON o.id = od.orders_id " +
           "WHERE o.status = 4 " +
           "AND (:startDate IS NULL OR o.created_at >= :startDate) " +
           "AND (:endDate IS NULL OR o.created_at <= :endDate) " +
           "GROUP BY c.id, c.name ", nativeQuery = true)
    List<Object[]> getSoldProductPercentByCategory(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
