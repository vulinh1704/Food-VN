package com.food_vn.repository.coupons;
import com.food_vn.model.conpons.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByNameAndIdNot(String name, Long id);
    Optional<Coupon> findByName(String name);

    @Query("""
        SELECT c FROM Coupon c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:type IS NULL OR LOWER(c.type) LIKE LOWER(CONCAT('%', :type, '%')))
        AND (:fromDate IS NULL OR c.fromDate >= :fromDate)
        AND (:toDate IS NULL OR c.toDate <= :toDate)
    """)
    Page<Coupon> searchCoupons(
            @Param("name") String name,
            @Param("type") String type,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable
    );
}
