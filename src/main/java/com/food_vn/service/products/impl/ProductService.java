package com.food_vn.service.products.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.lib.error_message.ERROR_MESSAGE;
import com.food_vn.model.categories.Category;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.products.Product;
import com.food_vn.model.products.ProductDTO;
import com.food_vn.repository.categories.CategoryRepository;
import com.food_vn.repository.coupons.CouponRepository;
import com.food_vn.repository.order_details.OrderDetailsRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.products.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService extends BaseService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

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
        Optional<Product> optProduct = productRepository.findById(id);
        optProduct.ifPresent(product -> {
            Date now = new Date();
            Set<Coupon> validCoupons = product.getCoupons().stream()
                    .filter(c ->
                            (c.getFromDate() == null || !now.before(c.getFromDate())) &&
                                    (c.getToDate() == null || !now.after(c.getToDate()))
                    )
                    .collect(Collectors.toSet());
            product.setCoupons(validCoupons);
        });
        return optProduct;
    }

    public Page<Product> getList(String name, Long minPrice, Long maxPrice, Set<Long> categoryIds, Set<Long> couponIds, Double minScore, Double maxScore, Pageable pageable) {
        return productRepository.getList(name, minPrice, maxPrice, categoryIds, couponIds, minScore, maxScore, pageable);
    }

    public Page<Product> getTopSellingProducts(int limit, int offset) {
        List<Object[]> topSelling = orderDetailsRepository.findTopSellingProducts(limit, offset);
        List<Long> productIds = topSelling.stream()
                .map(obj -> ((Number) obj[0]).longValue())
                .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));
        List<Product> orderedProducts = productIds.stream().map(productMap::get).filter(Objects::nonNull).collect(Collectors.toList());
        if (orderedProducts.size() < 10) {
            Set<Long> excludeIds = new HashSet<>(productIds);
            Page<Product> newest;
            if (excludeIds == null || excludeIds.isEmpty()) {
                newest = productRepository.findNewestProductsExcludeIds(null, org.springframework.data.domain.PageRequest.of(0, 10 - orderedProducts.size()));
            } else {
                newest = productRepository.findNewestProductsExcludeIds(excludeIds, org.springframework.data.domain.PageRequest.of(0, 10 - orderedProducts.size()));
            }
            for (Product p : newest.getContent()) {
                if (orderedProducts.size() >= 10) break;
                orderedProducts.add(p);
            }
        }
        List<Product> resultList = new ArrayList<>(orderedProducts);
        return new PageImpl<>(resultList, org.springframework.data.domain.PageRequest.of(offset / limit, limit), resultList.size());
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> get20ByCategoryOrNewest(Long categoryId) {
        Pageable limit20 = org.springframework.data.domain.PageRequest.of(0, 20);
        List<Product> byCategory = productRepository.findTop20ByCategoryIdOrderByCreatedAtDesc(categoryId, limit20);
        if (byCategory.size() >= 20) return byCategory;
        Set<Long> excludeIds = byCategory.stream().map(Product::getId).collect(java.util.stream.Collectors.toSet());
        int remain = 20 - byCategory.size();
        List<Product> newest = productRepository.findNewestProductsExcludeIds(excludeIds, org.springframework.data.domain.PageRequest.of(0, remain)).getContent();
        byCategory.addAll(newest);
        return byCategory;
    }
}
