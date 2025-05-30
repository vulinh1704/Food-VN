package com.food_vn.model.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.food_vn.lib.base_model.BaseModel;
import com.food_vn.model.address.Address;
import com.food_vn.model.customers.Customer;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.users.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orders extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Date date;

    @Column(nullable = false)
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(columnDefinition = "LONGTEXT")
    private String address;

    @Column()
    private int status;

    @Column(name = "cancellation_reason", columnDefinition = "LONGTEXT")
    private String cancellationReason;

    public Orders(Long id, Date date, Double total, User user, String address, int status, String cancellationReason) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.user = user;
        this.address = address;
        this.status = status;
        this.cancellationReason = cancellationReason;
    }

    public Orders() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
