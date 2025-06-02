package com.food_vn.service.orders;

public interface IOrdersService {
    default boolean hasUserOrderedProductWithStatus(Long userId, Long productId, int status) {
        return false;
    }
}
