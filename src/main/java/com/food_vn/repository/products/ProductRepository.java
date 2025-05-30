package com.food_vn.repository.products;

import com.food_vn.model.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND ((:minPrice IS NULL OR :maxPrice IS NULL) OR p.price BETWEEN :minPrice AND :maxPrice) " +
            "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds) " +
            "AND (:couponIds IS NULL OR EXISTS (" +
            "    SELECT c FROM p.coupons c " +
            "    WHERE c.id IN :couponIds " +
            "    AND (c.fromDate IS NULL OR c.fromDate <= CURRENT_DATE) " +
            "    AND (c.toDate IS NULL OR c.toDate >= CURRENT_DATE)" +
            ")) " +
            "AND (:minScore IS NULL OR p.score >= :minScore) " +
            "AND (:maxScore IS NULL OR p.score <= :maxScore)")
    Page<Product> getList(String name, Long minPrice, Long maxPrice, Set<Long> categoryIds, Set<Long> couponIds, Double minScore, Double maxScore, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (:excludeIds IS NULL OR p.id NOT IN :excludeIds) ORDER BY p.createdAt DESC")
    Page<Product> findNewestProductsExcludeIds(@Param("excludeIds") Set<Long> excludeIds, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.createdAt DESC")
    java.util.List<Product> findTop20ByCategoryIdOrderByCreatedAtDesc(@Param("categoryId") Long categoryId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.coupons c WHERE c.id = :couponId")
    java.util.List<Product> findAllByCouponId(@Param("couponId") Long couponId);
}
