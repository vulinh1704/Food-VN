package com.food_vn.model.conpons;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Coupon {
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
    private Date from;

    @Column()
    private Date to;

    public Coupon() {
    }

    public Coupon(Long id, Number discount, String type, String name, Date from, Date to) {
        this.id = id;
        this.discount = discount;
        this.type = type;
        this.name = name;
        this.from = from;
        this.to = to;
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

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
