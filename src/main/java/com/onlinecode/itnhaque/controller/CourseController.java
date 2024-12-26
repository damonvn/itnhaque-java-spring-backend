package com.onlinecode.itnhaque.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.response.ResCourseDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.service.CourseService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

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
    public ResponseEntity<Course> getById(@PathVariable("id") int id) throws IdInvalidException {
        Course course = this.courseService.fetchById(id);
        if (course == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(course);
    }

    @GetMapping("/course/update/{id}")
    @ApiMessage("Fetch course by id")
    public ResponseEntity<ResCourseDTO> getUpdateCourseById(@PathVariable("id") int id) throws IdInvalidException {
        ResCourseDTO course = this.courseService.fetchUpdateCourseById(id);
        if (course == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(course);
    }

    @GetMapping("/course")
    @ApiMessage("Fetch all courses")
    public ResponseEntity<ResultPaginationDTO> getCourses(
            @Filter Specification<Course> spec, Pageable pageable) {
        return ResponseEntity.ok(this.courseService.getCourses(spec, pageable));
    }

    @PutMapping("/course")
    @ApiMessage("Update a course")
    public ResponseEntity<ResCourseDTO> updateCourse(@Valid @RequestBody Course c) throws IdInvalidException {
        // check id
        if (this.courseService.fetchById(c.getId()) == null) {
            throw new IdInvalidException("Role with id = " + c.getId() + " does not exist");
        }
        return ResponseEntity.ok().body(this.courseService.update(c));
    }

    @PutMapping("/course/active")
    @ApiMessage("Update a course active")
    public ResponseEntity<Void> updateCourseActive(@Valid @RequestBody Course c) throws IdInvalidException {
        // check id
        if (this.courseService.fetchById(c.getId()) == null) {
            throw new IdInvalidException("Role with id = " + c.getId() + " does not exist");
        }
        return ResponseEntity.ok().body(this.courseService.updateCourseActive(c));
    }
}
