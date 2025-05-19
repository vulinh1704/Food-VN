package com.food_vn.controller.order_details;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.orders.Orders;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.service.coupons.impl.CouponService;
import com.food_vn.service.order_details.impl.OrderDetailService;
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
@RequestMapping("/order-details")
@CrossOrigin("*")
public class OrderDetailsController {
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrderDetail orderDetail, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        OrderDetail output = this.orderDetailService.save(orderDetail);
        ApiResponse<OrderDetail> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                output,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{id}/get-all")
    public ResponseEntity<?> getList(@PathVariable Long id) throws Exception {
        List<OrderDetail> list = orderDetailService.findAllByOrderId(id);
        ApiResponse<List<OrderDetail>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                list,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/orders/{order_id}/products/{product_id}/delete")
    public ResponseEntity<?> delete(
            @PathVariable Long order_id,
            @PathVariable Long product_id
    ) {
        orderDetailService.deleteByProductIdAndOrderId(order_id, product_id);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
