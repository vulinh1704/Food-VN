package com.food_vn.model.orders;

import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.users.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class OrderResponse {
    private Long id;
    private Date date;
    private Double total;
    private User user;
    private String address;
    private int status;
    private List<OrderDetail> orderDetails;
    private String cancellationReason;

    public OrderResponse(Long id, Date date, Double total, User user, String address, int status, List<OrderDetail> orderDetails, String cancellationReason) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.user = user;
        this.address = address;
        this.status = status;
        this.orderDetails = orderDetails;
        this.cancellationReason = cancellationReason;
    }

    public OrderResponse() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
