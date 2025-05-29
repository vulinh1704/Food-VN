package com.food_vn.service.order_details;

import com.food_vn.model.order_details.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> findAllByOrderId(Long orderId);
    OrderDetail save(OrderDetail orderDetail);

    /**
     * Thống kê phần trăm loại sản phẩm bán ra theo ngày, tháng, năm
     * @param startDate ngày bắt đầu (yyyy-MM-dd), có thể null
     * @param endDate ngày kết thúc (yyyy-MM-dd), có thể null
     * @return danh sách thống kê phần trăm loại sản phẩm
     */
    List<Object> getProductCategorySalePercent(String startDate, String endDate);
}
