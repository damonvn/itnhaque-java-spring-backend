package com.onlinecode.itnhaque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.onlinecode.itnhaque.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>,
                JpaSpecificationExecutor<Category> {
        boolean existsByValue(String value);

        boolean existsByName(String name);
}
