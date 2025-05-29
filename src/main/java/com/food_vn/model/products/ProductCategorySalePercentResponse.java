package com.food_vn.model.products;

public class ProductCategorySalePercentResponse {
    private Long categoryId;
    private String categoryName;
    private Long totalSold;
    private double percent;

    public ProductCategorySalePercentResponse(Long categoryId, String categoryName, Long totalSold, double percent) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalSold = totalSold;
        this.percent = percent;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}

