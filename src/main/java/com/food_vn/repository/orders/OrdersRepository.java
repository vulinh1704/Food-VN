package com.food_vn.repository.orders;

import com.food_vn.model.orders.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByUserIdAndStatus(Long userId, int status);

    List<Orders> findAllByUserIdAndStatus(Long userId, int status);

    @Query("SELECT o FROM Orders o " +
            "WHERE o.status <> 1 " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:startDate IS NULL OR o.date >= :startDate) " +
            "AND (:endDate IS NULL OR o.date <= :endDate)")
    Page<Orders> findByDateRangeAndStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") Integer status,
            Pageable pageable
    );

    @Query("SELECT o FROM Orders o " +
            "WHERE o.status <> 1 " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:startDate IS NULL OR o.date >= :startDate) " +
            "AND (:endDate IS NULL OR o.date <= :endDate) " +
            "AND (:userId IS NULL OR o.user.id = :userId)")
    Page<Orders> findByDateRangeStatusAndUserId(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") Integer status,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT SUM(o.total) FROM Orders o WHERE o.status = 4 AND DATE(o.date) = :date")
    Double sumTotalByDate(@Param("date") Date date);
}
