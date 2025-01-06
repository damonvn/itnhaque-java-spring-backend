package com.onlinecode.itnhaque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.repository.CourseRepository;

@Service
public class ClientService {
    private final CourseRepository courseRepository;

    public ClientService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> fetchActiveCourses() {
        return this.courseRepository.findByActiveTrue();
    }
}
