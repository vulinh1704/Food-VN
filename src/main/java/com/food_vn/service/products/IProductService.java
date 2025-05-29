package com.food_vn.service.products;

import com.food_vn.model.products.Product;
import com.food_vn.model.products.ProductDTO;
import java.util.Optional;

public interface IProductService {
    Product save(ProductDTO dto);

    Optional<Product> findById(Long id);

    java.util.List<com.food_vn.model.products.Product> get20ByCategoryOrNewest(Long categoryId);
}

