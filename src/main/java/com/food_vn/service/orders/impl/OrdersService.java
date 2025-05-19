package com.food_vn.service.orders.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.orders.Orders;
import com.food_vn.model.orders.OrdersDTO;
import com.food_vn.repository.orders.OrdersRepository;
import com.food_vn.service.orders.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends BaseService implements IOrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public Orders getCard(Long userId) {
        return ordersRepository.findByUserIdAndStatus(userId, 1);
    }

    public void buy(OrdersDTO ordersDTO) {
    }

    private void saveOrderDetails(OrdersDTO ordersDTO) {

    }
}
