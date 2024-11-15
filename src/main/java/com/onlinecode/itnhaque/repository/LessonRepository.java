package com.onlinecode.itnhaque.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.onlinecode.itnhaque.domain.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer>,
                JpaSpecificationExecutor<Lesson> {
}
