package com.food_vn.service.order_details.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.products.ProductCategorySalePercentResponse;
import com.food_vn.repository.order_details.OrderDetailsRepository;
import com.food_vn.service.order_details.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<OrderDetail> od = orderDetailsRepository.findByOrdersIdAndProductId(orderDetail.getOrders().getId(), orderDetail.getProduct().getId());
        if (!od.isEmpty()) {
            OrderDetail toUpdate = od.get(0);
            toUpdate.setQuantity(toUpdate.getQuantity() + orderDetail.getQuantity());
            orderDetail = toUpdate;
        }
        return orderDetailsRepository.save(orderDetail);
    }

    public void deleteByProductIdAndOrderId(Long orderId, Long productId) {
        List<OrderDetail> od = orderDetailsRepository.findByOrdersIdAndProductId(orderId, productId);
        if (!od.isEmpty()) {
            OrderDetail toDelete = od.get(0);
            orderDetailsRepository.delete(toDelete);
        }
    }

    @Override
    public List<Object> getProductCategorySalePercent(String startDate, String endDate) {
        List<Object[]> rawData = orderDetailsRepository.getSoldProductPercentByCategory(startDate, endDate);
        long totalSold = 0L;
        for (Object[] row : rawData) {
            totalSold += row[2] != null ? ((Number) row[2]).longValue() : 0L;
        }
        List<Object> result = new ArrayList<>();
        for (Object[] row : rawData) {
            Long categoryId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String categoryName = Objects.toString(row[1], "");
            Long sold = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            double percent = totalSold > 0 ? (sold * 100.0 / totalSold) : 0.0;
            result.add(new ProductCategorySalePercentResponse(categoryId, categoryName, sold, percent));
        }
        return result;
    }
}
