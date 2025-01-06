package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.service.CourseService;
import com.onlinecode.itnhaque.service.ClientService;
import com.onlinecode.itnhaque.service.ContentService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ClientController {

    private final CourseService courseService;
    private final ContentService contentService;
    private final ClientService clientService;

    public ClientController(CourseService courseService, ContentService contentService,
            ClientService clientService) {
        this.courseService = courseService;
        this.contentService = contentService;
        this.clientService = clientService;
    }

    @GetMapping("/client/course/{id}")
    @ApiMessage("Fetch course by id")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") int id) throws IdInvalidException {
        Course course = this.courseService.clientFetchById(id);
        return ResponseEntity.ok().body(course);
    }

    @GetMapping("/client/content/{id}")
    @ApiMessage("Fetch content by id")
    public ResponseEntity<Content> getContentById(@PathVariable("id") int id) throws IdInvalidException {
        Content content = this.contentService.clientFetchById(id);
        return ResponseEntity.ok().body(content);
    }

    @GetMapping("client/course")
    @ApiMessage("Fetch actived course")
    public ResponseEntity<List<Course>> fetchActiveCourse() {
        return ResponseEntity.ok().body(this.clientService.fetchActiveCourses());
    }
}
