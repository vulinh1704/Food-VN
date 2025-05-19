package com.food_vn.service.coupons;

import com.food_vn.model.conpons.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICouponService {
    Coupon save(Coupon coupon) throws Exception;

    Optional<Coupon> findById(Long id);

    Page<Coupon> getList(String name, String type, Date fromDate, Date toDate, Pageable pageable);

    List<Coupon> getAll();
}
