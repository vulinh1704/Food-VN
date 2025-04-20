package com.food_vn.service.products;

import com.food_vn.model.products.Product;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IProductService  extends UserDetailsService {
    void save(Product product);

    Iterable<Product> findTopProducts();

    Iterable<Product> findAll();

    Optional<Product> findById(Long id);
}