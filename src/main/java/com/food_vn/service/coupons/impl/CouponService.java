package com.food_vn.service.coupons.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.lib.error_message.ERROR_MESSAGE;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.products.Product;
import com.food_vn.repository.coupons.CouponRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.coupons.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService extends BaseService implements ICouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Coupon save(Coupon coupon) throws Exception {
        if (this.isExist(coupon.getId())) {
            Optional<Coupon> oldCoupon = this.couponRepository.findById(coupon.getId());
            this.isAssert(oldCoupon.isPresent(), ERROR_MESSAGE.DATA_NOT_FOUND);
        }
        Optional<Coupon> oldCoupon;
        if (this.isExist(coupon.getId())) {
            oldCoupon = this.couponRepository.findByNameAndIdNot(coupon.getName(), coupon.getId());
        } else {
            oldCoupon = this.couponRepository.findByName(coupon.getName());
        }
        this.isAssert(oldCoupon.isEmpty(), ERROR_MESSAGE.DATA_ALREADY_EXISTS);
        return couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponRepository.findById(id);
    }

    public Page<Coupon> getList(String name, String type, Date fromDate, Date toDate, Pageable pageable) {
        return couponRepository.searchCoupons(name, type, fromDate, toDate, pageable);
    }

    public List<Coupon> getAll() {
        Date now = new Date();
        return couponRepository.findAll().stream()
            .filter(coupon -> (coupon.getFromDate() == null || !coupon.getFromDate().after(now))
                && (coupon.getToDate() == null || !coupon.getToDate().before(now)))
            .toList();
    }

    public void delete(Long id) {
        Optional<Coupon> oldCoupon = this.couponRepository.findById(id);
        this.isAssert(oldCoupon.isPresent(), ERROR_MESSAGE.DATA_NOT_FOUND);
        java.util.List<Product> products = productRepository.findAllByCouponId(id);
        for (Product product : products) {
            if (product.getCoupons() != null && product.getCoupons().removeIf(c -> c.getId().equals(id))) {
                productRepository.save(product);
            }
        }
        couponRepository.deleteById(id);
    }
}
