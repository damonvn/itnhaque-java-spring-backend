package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category c) {
        return this.categoryRepository.save(c);
    }

    public List<Category> fetchAllCategories() {
        return this.categoryRepository.findAll();
    }

    public boolean existByValue(String value) {
        return this.categoryRepository.existsByValue(value);
    }

    public Category fetchById(int id) {
        Optional<Category> roleOptional = this.categoryRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }
}
