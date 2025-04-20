package com.food_vn.model.products;

import com.food_vn.model.categories.Category;
import com.food_vn.model.evaluates.Evaluate;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String images;

    @Column(nullable = false)
    private Number price;

    @Column(nullable = false)
    private String description;

    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_evaluate",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "evaluate_id")})
    private Set<Evaluate> evaluates;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 5")
    private double score;

    public Product(Long id, String name, String images, Number price, String description, boolean enabled, Category category, Set<Evaluate> evaluates, double score) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.price = price;
        this.description = description;
        this.enabled = enabled;
        this.category = category;
        this.evaluates = evaluates;
        this.score = score;
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

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
}
