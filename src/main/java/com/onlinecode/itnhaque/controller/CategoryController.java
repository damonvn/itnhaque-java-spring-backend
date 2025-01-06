package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.service.CategoryService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    @ApiMessage("Create a category")
    public ResponseEntity<Category> create(@Valid @RequestBody Category c) throws IdInvalidException {
        if (this.categoryService.existByValue(c.getValue())) {
            throw new IdInvalidException("Category with value = " + c.getValue() + " already existed ");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.create(c));
    }

    @GetMapping("/category")
    @ApiMessage("Fetch all categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok().body(this.categoryService.fetchAllCategories());
    }
}
