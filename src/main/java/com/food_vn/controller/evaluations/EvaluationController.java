package com.food_vn.controller.evaluations;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.evaluates.Evaluation;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.service.evaluations.IEvaluationService;
import com.food_vn.service.orders.IOrdersService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Evaluation evaluation) throws Exception {
        Evaluation output = evaluationService.save(evaluation);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                output,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProduct(@PathVariable Long productId) {
        List<Evaluation> evaluations = evaluationService.findAllByProduct_Id(productId);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                evaluations,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/has-ordered/product/{productId}")
    public ResponseEntity<?> hasUserOrderedProductWithStatus(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        boolean output = ordersService.hasUserOrderedProductWithStatus(userDetails.getId(), productId, 4);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                output,
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestEvaluations() {
        List<Evaluation> evaluations = evaluationService.findTop10Newest();
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                evaluations,
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
