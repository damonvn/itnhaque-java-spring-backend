package com.onlinecode.itnhaque.service;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.repository.CourseRepository;

import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course create(Course c) {
        return this.courseRepository.save(c);
    }

    public Course fetchById(long id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        }
        return null;
    }

}
