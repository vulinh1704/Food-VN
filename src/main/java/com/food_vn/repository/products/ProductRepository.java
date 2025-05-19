package com.food_vn.repository.products;


import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Long countProductsByCategoryId(Long id);

    Optional<Product> findByNameAndIdNot(String name, Long id);
    Optional<Product> findByName(String name);

    @Query("SELECT COUNT(p) FROM Product p JOIN p.coupons c WHERE c.id = :couponId")
    Long countByCouponId(@Param("couponId") Long couponId);

    @EntityGraph(attributePaths = {"coupons", "category"})
    Optional<Product> findById(Long id);

    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) " +
            "AND (p.price BETWEEN :minPrice AND :maxPrice OR :minPrice IS NULL OR :maxPrice IS NULL) " +
            "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds) " +
            "AND (:couponIds IS NULL OR EXISTS (SELECT c FROM p.coupons c WHERE c.id IN :couponIds)) " +
            "AND p.score BETWEEN :minScore AND :maxScore")
    Page<Product> getList(String name, Long minPrice, Long maxPrice, Set<Long> categoryIds, Set<Long> couponIds, Double minScore, Double maxScore, Pageable pageable);
}