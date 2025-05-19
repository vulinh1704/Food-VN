package com.food_vn.service.products.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.lib.error_message.ERROR_MESSAGE;
import com.food_vn.model.categories.Category;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.products.Product;
import com.food_vn.model.products.ProductDTO;
import com.food_vn.repository.categories.CategoryRepository;
import com.food_vn.repository.coupons.CouponRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.products.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService extends BaseService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public Product save(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name);
        product.setPrice(dto.price);
        product.setDescription(dto.description);
        product.setImages(dto.images);
        product.setQuantity(dto.getQuantity());

        Category category = categoryRepository.findById(dto.categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        if (dto.couponIds != null && !dto.couponIds.isEmpty()) {
            Set<Coupon> coupons = new HashSet<>(couponRepository.findAllById(dto.couponIds));
            product.setCoupons(coupons);
        }

        if (this.isExist(dto.getId())) {
            product.setId(dto.getId());
            Optional<Product> oldProduct = this.productRepository.findById(dto.getId());
            this.isAssert(oldProduct.isPresent(), ERROR_MESSAGE.DATA_NOT_FOUND);
        }
        Optional<Product> oldProduct;
        if (this.isExist(dto.getId())) {
            oldProduct = this.productRepository.findByNameAndIdNot(product.getName(), product.getId());
        } else {
            oldProduct = this.productRepository.findByName(dto.getName());
        }
        this.isAssert(oldProduct.isEmpty(), ERROR_MESSAGE.DATA_ALREADY_EXISTS);

        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getList(String name, Long minPrice, Long maxPrice, Set<Long> categoryIds, Set<Long> couponIds, Double minScore, Double maxScore, Pageable pageable) {
        return productRepository.getList(name, minPrice, maxPrice, categoryIds, couponIds, minScore, maxScore, pageable);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
