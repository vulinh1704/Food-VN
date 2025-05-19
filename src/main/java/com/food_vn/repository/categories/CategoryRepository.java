package com.food_vn.repository.categories;

import com.food_vn.model.categories.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Category> findByName(String name);
    List<Category> findAllByNameContaining(String name);
    Optional<Category> findByNameAndIdNot(String name, Long id);
}