package com.food_vn.controller.products;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.categories.Category;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.products.Product;
import com.food_vn.model.products.ProductDTO;
import com.food_vn.service.categories.impl.CategoryService;
import com.food_vn.service.products.impl.ProductService;
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
import java.util.Optional;
import java.util.Set;

@RestController()
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        Product categoryOutput = this.productService.save(productDTO);
        ApiResponse<Product> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                categoryOutput,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/get-list")
    public ResponseEntity<?> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Set<Long> categoryIds,
            @RequestParam(required = false) Set<Long> couponIds,
            @RequestParam(defaultValue = "1") Double minScore,
            @RequestParam(defaultValue = "5") Double maxScore,
            @RequestParam(defaultValue = "0") Long minPrice,
            @RequestParam(defaultValue = "99999999999999") Long maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productService.getList(name, minPrice, maxPrice, categoryIds, couponIds, minScore, maxScore, pageable);

        ApiResponse<Page<Product>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                productPage,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getOneById(
            @PathVariable Long id
    ) {
        Optional<Product> productOptional = productService.findById(id);

        ApiResponse<Product> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                productOptional.get(),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        productService.delete(id);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-selling")
    public ResponseEntity<?> getTopSellingProducts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        int offset = page * size;
        Page<Product> products = productService.getTopSellingProducts(size, offset);
        ApiResponse<Page<Product>> response = new ApiResponse<>(
                "Top selling products fetched successfully",
                products,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-category-or-newest")
    public ResponseEntity<?> get20ByCategoryOrNewest(@RequestParam Long categoryId) {
        var products = productService.get20ByCategoryOrNewest(categoryId);
        java.util.Date now = new java.util.Date();
        for (Product p : products) {
            if (p.getCoupons() != null) {
                p.setCoupons(p.getCoupons().stream()
                    .filter(c -> (c.getFromDate() == null || !now.before(c.getFromDate()))
                              && (c.getToDate() == null || !now.after(c.getToDate())))
                    .collect(java.util.stream.Collectors.toSet()));
            }
        }
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                products,
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
