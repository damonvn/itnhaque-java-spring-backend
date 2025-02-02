package com.onlinecode.itnhaque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.onlinecode.itnhaque.domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer>,
                JpaSpecificationExecutor<Chapter> {

        // Tìm Chapter theo indexInCourse
        Chapter findByIndexInCourse(int indexInCourse);
}
