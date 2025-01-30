package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.service.CategoryService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @PutMapping("/category")
    @ApiMessage("Update a category")
    public ResponseEntity<Category> update(@Valid @RequestBody Category c) throws IdInvalidException {
        Category categoryDB = this.categoryService.fetchById(c.getId());
        // check id
        if (categoryDB == null) {
            throw new IdInvalidException("Category with id = " + c.getId() + " does not exist");
        }

        String name = categoryDB.getName();
        // !name.equals(c.getName()) &&
        if (this.categoryService.existByName(c.getName())) {
            throw new IdInvalidException("The name = " + c.getName() + " already exists");
        }
        String value = categoryDB.getValue();
        if (!value.equals(c.getValue()) && this.categoryService.existByValue(c.getValue())) {
            throw new IdInvalidException("The value = " + c.getValue() + " already exists");
        }
        categoryDB.setName(c.getName());
        categoryDB.setValue(c.getValue());
        return ResponseEntity.ok().body(this.categoryService.update(categoryDB));
    }

    @GetMapping("/category/{id}")
    @ApiMessage("Fetch category by id")
    public ResponseEntity<Category> getById(@PathVariable("id") int id) throws IdInvalidException {
        Category category = this.categoryService.fetchById(id);
        if (category == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/category/all")
    @ApiMessage("Fetch all categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok().body(this.categoryService.fetchAllCategories());
    }

    @GetMapping("/category")
    @ApiMessage("Fetch categories pagination")
    public ResponseEntity<ResultPaginationDTO> fetchRoles(
            @Filter Specification<Category> spec, Pageable pageable) {
        return ResponseEntity.ok(this.categoryService.fetchCateogriesPagination(spec, pageable));
    }

    @DeleteMapping("/category/{id}")
    @ApiMessage("Delete a category")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) throws IdInvalidException {
        // check id
        if (this.categoryService.fetchById(id) == null) {
            throw new IdInvalidException("Category with id = " + id + " does not exist");
        }
        this.categoryService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
