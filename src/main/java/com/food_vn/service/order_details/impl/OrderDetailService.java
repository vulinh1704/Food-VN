package com.food_vn.service.order_details.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.repository.order_details.OrderDetailsRepository;
import com.food_vn.service.order_details.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService extends BaseService implements IOrderDetailService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<OrderDetail> findAllByOrderId(Long orderId) {
        return orderDetailsRepository.findByOrdersId(orderId);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailsRepository.save(orderDetail);
    }

    public void deleteByProductIdAndOrderId(Long orderId, Long productId) {
        List<OrderDetail> od = orderDetailsRepository.findByOrdersIdAndProductId(orderId, productId);
        if (!od.isEmpty()) {
            OrderDetail toDelete = od.get(0);
            orderDetailsRepository.delete(toDelete);
        }
    }
}
