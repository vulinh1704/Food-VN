package com.food_vn.service.categories;

import com.food_vn.model.categories.Category;

import java.util.Optional;

public interface ICategoryService {
    Category save(Category category) throws Exception;

    Optional<Category> findById(Long id);
}
