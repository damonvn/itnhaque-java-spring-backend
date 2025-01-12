package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.request.ReqContentId;
import com.onlinecode.itnhaque.domain.response.ResContentDTO;
import com.onlinecode.itnhaque.domain.response.ResLessonParameters;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.service.CourseService;
import com.onlinecode.itnhaque.service.ClientService;
import com.onlinecode.itnhaque.service.ContentService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/client/content/dto/{id}")
    @ApiMessage("Fetch content by id")
    public ResponseEntity<ResContentDTO> getContentDTOById(@PathVariable("id") int id) throws IdInvalidException {
        ResContentDTO content = this.contentService.fetchClientResContentDTOById(id);
        return ResponseEntity.ok().body(content);
    }

    @GetMapping("/client/course")
    @ApiMessage("Fetch actived course")
    public ResponseEntity<List<Course>> fetchActiveCourse() {
        return ResponseEntity.ok().body(this.clientService.fetchActiveCourses());
    }

    @PostMapping("/client/lesson/parameters")
    @ApiMessage("Fetch lesson parameter")
    public ResponseEntity<ResLessonParameters> fetchLessonParameters(@Valid @RequestBody ReqContentId contentId) {
        ResLessonParameters res = this.clientService.getLessonParameters(contentId.getContentId());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/client/lesson/next")
    @ApiMessage("Get next lesson")
    public ResponseEntity<Integer> findNextLesson(@Valid @RequestBody ReqContentId contentId) {
        int res = this.clientService.findNextLesson(contentId.getContentId());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/client/lesson/previous")
    @ApiMessage("Get previouse lesson")
    public ResponseEntity<Integer> findPreviousLesson(@Valid @RequestBody ReqContentId contentId) {
        int res = this.clientService.findPreviousLesson(contentId.getContentId());
        return ResponseEntity.ok().body(res);
    }
}
