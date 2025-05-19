package com.food_vn.model.orders;

import com.food_vn.model.order_details.OrderDetail;

import java.util.Date;

public class OrdersDTO {
    private Long id;
    private Date date;
    private Double total;
    private Long userId;
    private String address;
    private int status;
    private OrderDetail[] orderDetails;

    public OrdersDTO(Long id, Date date, Double total, Long userId, String address, int status, OrderDetail[] orderDetails) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.userId = userId;
        this.address = address;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public OrdersDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderDetail[] getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetail[] orderDetails) {
        this.orderDetails = orderDetails;
    }
}
