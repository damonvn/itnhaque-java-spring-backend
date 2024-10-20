package com.onlinecode.itnhaque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.onlinecode.itnhaque.domain.LessonContent;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContent, Long>,
        JpaSpecificationExecutor<LessonContent> {

}
