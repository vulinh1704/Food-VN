package com.food_vn.service.order_details;

import com.food_vn.model.order_details.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> findAllByOrderId(Long orderId);
    OrderDetail save(OrderDetail orderDetail);
}
