package com.food_vn.service.products;

import com.food_vn.model.products.Product;
import com.food_vn.model.products.ProductDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IProductService {
    Product save(ProductDTO dto);

    Optional<Product> findById(Long id);

}