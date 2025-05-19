package com.food_vn.controller.coupons;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.service.coupons.ICouponService;
import com.food_vn.service.coupons.impl.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/coupons")
@CrossOrigin("*")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Coupon coupon, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        Coupon couponOutput = this.couponService.save(coupon);
        ApiResponse<Coupon> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                couponOutput,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-list")
    public ResponseEntity<?> getList(
            @RequestParam(defaultValue = PAGEABLE.PAGE_OFFSET) int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = PAGEABLE.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            Date fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            Date toDate,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Coupon> couponPage = couponService.getList(name, type, fromDate, toDate, pageable);

        ApiResponse<Page<Coupon>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                couponPage,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getOneById(
            @PathVariable Long id
    ) {
        Optional<Coupon> couponOptional = couponService.findById(id);

        ApiResponse<Coupon> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                couponOptional.get(),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        List<Coupon> list = couponService.getAll();
        ApiResponse<List<Coupon>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                list,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        couponService.delete(id);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
