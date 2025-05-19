package com.food_vn.controller.categories;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.lib.app_const.PAGEABLE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.categories.Category;
import com.food_vn.service.categories.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Category category, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new RuntimeException(API_RESPONSE.INVALID_INPUT_MESSAGE);
        Category categoryOutput = this.categoryService.save(category);
        ApiResponse<Category> response = new ApiResponse<>(
                API_RESPONSE.SAVED_SUCCESS_MESSAGE,
                categoryOutput,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-list")
    public ResponseEntity<?> getList(
            @RequestParam(defaultValue = PAGEABLE.PAGE_OFFSET) int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = PAGEABLE.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoryPage = categoryService.getAllCategories(name, pageable);

        ApiResponse<Page<Category>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                categoryPage,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "") String name
    ) {

        List<Category> list = categoryService.getAllByName(name);
        ApiResponse<List<Category>> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                list,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getOneById(
            @PathVariable Long id
    ) {
        Optional<Category> categoryOptional = categoryService.findById(id);

        ApiResponse<Category> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                categoryOptional.get(),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        categoryService.delete(id);
        ApiResponse response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
