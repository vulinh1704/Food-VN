package com.food_vn.model.conpons;
import com.food_vn.lib.base_model.BaseModel;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Coupon extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Number discount;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column()
    private Date fromDate;

    @Column()
    private Date toDate;

    public Coupon() {
    }

    public Coupon(Long id, Number discount, String type, String name, Date from, Date to) {
        this.id = id;
        this.discount = discount;
        this.type = type;
        this.name = name;
        this.fromDate = from;
        this.toDate = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Number getDiscount() {
        return discount;
    }

    public void setDiscount(Number discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date from) {
        this.fromDate = from;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date to) {
        this.toDate = to;
    }
}
