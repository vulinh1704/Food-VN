package com.food_vn.repository.orders;

import com.food_vn.model.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByUserIdAndStatus(Long userId, int status);
}
