package com.onlinecode.itnhaque.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.service.CourseService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/course")
    @ApiMessage("Create a course")
    public ResponseEntity<Course> create(@Valid @RequestBody Course c) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.create(c));
    }

    @GetMapping("/course/{id}")
    @ApiMessage("Fetch course by id")
    public ResponseEntity<Course> getById(@PathVariable("id") long id) throws IdInvalidException {
        Course course = this.courseService.fetchById(id);
        if (course == null) {
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(course);
    }
}
