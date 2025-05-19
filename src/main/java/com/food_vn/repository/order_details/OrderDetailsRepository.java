package com.food_vn.repository.order_details;

import com.food_vn.model.order_details.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrdersId(Long orderId);

    List<OrderDetail> findByOrdersIdAndProductId(Long orderId, Long productId);
}
