package com.food_vn.controller.orders;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.orders.Orders;
import com.food_vn.model.orders.OrdersDTO;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.service.coupons.impl.CouponService;
import com.food_vn.service.orders.impl.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/get-card")
    public ResponseEntity<?> getCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        Orders orders = ordersService.getCard(userDetails.getId());
        ApiResponse<Orders> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                orders,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody OrdersDTO ordersDTO) {
        ordersService.buy(ordersDTO);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
