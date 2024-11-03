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
import com.onlinecode.itnhaque.domain.Role;
import com.onlinecode.itnhaque.service.LessonContentService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class LessonContentController {
    private final LessonContentService lessonContentService;

    public LessonContentController(LessonContentService lessonContentService) {
        this.lessonContentService = lessonContentService;
    }

    @PostMapping("/lesson")
    @ApiMessage("Create a lesson content")
    public ResponseEntity<Content> create(@Valid @RequestBody Content l) throws IdInvalidException {
        // check name
        return ResponseEntity.status(HttpStatus.CREATED).body(this.lessonContentService.create(l));
    }

    @GetMapping("/lesson/{id}")
    @ApiMessage("Fetch lesson by id")
    public ResponseEntity<Content> getById(@PathVariable("id") int id) throws IdInvalidException {
        Content lesson = this.lessonContentService.fetchById(id);
        if (lesson == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(lesson);
    }
}
