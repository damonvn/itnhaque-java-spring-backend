package com.onlinecode.itnhaque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.onlinecode.itnhaque.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer>,
                JpaSpecificationExecutor<Skill> {
        boolean existsByValue(String value);
}
