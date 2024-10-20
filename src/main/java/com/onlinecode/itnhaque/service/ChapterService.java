package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.repository.ChapterRepository;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseService courseService;

    public ChapterService(ChapterRepository chapterRepository, CourseService courseService) {
        this.chapterRepository = chapterRepository;
        this.courseService = courseService;
    }

    public Chapter create(Chapter c) {
        if (c.getCourse() != null) {
            Course course = this.courseService.fetchById(c.getCourse().getId());
            c.setCourse(course != null ? course : null);
        }
        return this.chapterRepository.save(c);
    }

    public Chapter fetchById(long id) {
        Optional<Chapter> chapterOptional = this.chapterRepository.findById(id);
        if (chapterOptional.isPresent()) {
            return chapterOptional.get();
        }
        return null;
    }

}
