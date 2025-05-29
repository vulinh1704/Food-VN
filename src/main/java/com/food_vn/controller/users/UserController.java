package com.food_vn.controller.users;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.products.Product;
import com.food_vn.model.users.JwtResponse;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserDTO;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.model.users.ChangePasswordRequest;
import com.food_vn.service.users.IUserService;
import com.food_vn.service.users.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        userService.register(user);
        ApiResponse<User> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername());
        ApiResponse<JwtResponse> response = new ApiResponse<>(
                API_RESPONSE.LOGIN_SUCCESS_MESSAGE,
                new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()),
                HttpStatus.ACCEPTED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/get-info")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        Optional<User> user = userService.findById(userDetails.getId());
        if (user.isEmpty()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        ApiResponse<User> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                user.get(),
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/users/update-info")
    public ResponseEntity<?> update(@RequestBody UserDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        user.setId(userDetails.getId());
        User userOutput = userService.save(user);
        ApiResponse<User> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                userOutput,
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/users/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<String> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        boolean result = userService.changePassword(userDetails.getId(), request.getOldPassword(), request.getNewPassword());
        if (!result) {
            ApiResponse<String> response = new ApiResponse<>(
                    "Old password is incorrect!",
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ApiResponse<String> response = new ApiResponse<>(
                "Password changed successfully!",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
