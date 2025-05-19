package com.food_vn.model.products;

import com.food_vn.lib.base_model.BaseModel;
import com.food_vn.model.categories.Category;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.evaluates.Evaluate;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Product extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String images;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_evaluate",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "evaluate_id")})
    private Set<Evaluate> evaluates;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 5")
    private double score = 5;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_coupon",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "coupon_id")})
    private Set<Coupon> coupons;

    @Column(nullable = false)
    private int quantity;

    public Product(Long id, String name, String images, Long price, String description, Category category, Set<Evaluate> evaluates, double score, Set<Coupon> coupons, int quantity) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.price = price;
        this.description = description;
        this.category = category;
        this.evaluates = evaluates;
        this.score = score;
        this.coupons = coupons;
        this.quantity = quantity;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Evaluate> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(Set<Evaluate> evaluates) {
        this.evaluates = evaluates;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
