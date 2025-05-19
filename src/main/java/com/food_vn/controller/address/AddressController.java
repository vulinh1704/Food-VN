package com.food_vn.controller.address;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.address.Address;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.service.address.impl.AddressService;
import com.food_vn.service.coupons.impl.CouponService;
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
@RequestMapping("/addresses")
@CrossOrigin("*")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Address address, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        Address addressOutput = this.addressService.save(address);
        ApiResponse<Address> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                addressOutput,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        List<Address> addresses = addressService.findAll(userDetails.getId());

        ApiResponse<List<Address>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                addresses,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getOneById(
            @PathVariable Long id
    ) {
        Optional<Address> addressOptional = addressService.findById(id);

        ApiResponse<Address> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                addressOptional.get(),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        addressService.deleteById(id);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
