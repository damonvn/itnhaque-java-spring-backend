package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
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

    public Category update(Category c) {
        Category cDB = this.fetchById(c.getId());
        cDB.setName(c.getName());
        cDB.setValue(c.getValue());
        Category cUpdate = this.categoryRepository.save(cDB);
        return cUpdate;
    }

    public boolean existByName(String name) {
        return this.categoryRepository.existsByName(name);
    }

    public ResultPaginationDTO fetchCateogriesPagination(Specification<Category> spec, Pageable pageable) {
        Page<Category> pCategory = this.categoryRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCategory.getTotalPages());
        mt.setTotal(pCategory.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCategory.getContent());
        return rs;
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

    public void delete(int id) {
        this.categoryRepository.deleteById(id);
    }
}
