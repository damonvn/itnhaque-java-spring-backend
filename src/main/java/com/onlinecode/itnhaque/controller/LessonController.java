package com.onlinecode.itnhaque.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.domain.request.ReqUpdateLessonVideo;
import com.onlinecode.itnhaque.service.LessonService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/lesson")
    @ApiMessage("Create a lesson")
    public ResponseEntity<Content> create(@Valid @RequestBody Lesson l) throws IdInvalidException {
        // check name
        return ResponseEntity.status(HttpStatus.CREATED).body(this.lessonService.create(l));
    }

    @GetMapping("/lesson/{id}")
    @ApiMessage("Fetch lesson by id")
    public ResponseEntity<Lesson> getById(@PathVariable("id") int id) throws IdInvalidException {
        Lesson lesson = this.lessonService.fetchById(id);
        if (lesson == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(lesson);
    }

    @PutMapping("/lesson/update")
    @ApiMessage("Update lesson")
    public ResponseEntity<Content> updateLessonContent(@Valid @RequestBody Content c)
            throws IdInvalidException {
        Content content = this.lessonService.handleUpdateLessonContent(c);
        return ResponseEntity.ok().body(content);
    }

    @PutMapping("/lesson/video")
    @ApiMessage("Update lesson video")
    public ResponseEntity<Content> updateLessonVideo(@Valid @RequestBody ReqUpdateLessonVideo req)
            throws IdInvalidException {
        Content content = this.lessonService.handleUpdateLessonVideo(req);
        return ResponseEntity.ok().body(content);
    }
}
