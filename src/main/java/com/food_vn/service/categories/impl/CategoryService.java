package com.food_vn.service.categories.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.lib.error_message.ERROR_MESSAGE;
import com.food_vn.model.categories.Category;
import com.food_vn.repository.categories.CategoryRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.categories.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService extends BaseService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category save(Category category) throws Exception {
        if (this.isExist(category.getId())) {
            Optional<Category> oldCategory = this.categoryRepository.findById(category.getId());
            this.isAssert(oldCategory.isPresent(), ERROR_MESSAGE.DATA_NOT_FOUND);
        }
        Optional<Category> oldCategory;
        if (this.isExist(category.getId())) {
            oldCategory = this.categoryRepository.findByNameAndIdNot(category.getName(), category.getId());
        } else {
            oldCategory = this.categoryRepository.findByName(category.getName());
        }
        this.isAssert(oldCategory.isEmpty(), ERROR_MESSAGE.DATA_ALREADY_EXISTS);
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> getAllCategories(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void delete(Long id) {
        Optional<Category> oldCategory = this.categoryRepository.findById(id);
        this.isAssert(oldCategory.isPresent(), ERROR_MESSAGE.DATA_NOT_FOUND);
        Long numberOfProduct = this.productRepository.countProductsByCategoryId(id);
        this.isAssert(numberOfProduct == 0, ERROR_MESSAGE.CATEGORY_IN_USER);
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllByName(String name) {
        return categoryRepository.findAllByNameContaining(name);
    }
}
