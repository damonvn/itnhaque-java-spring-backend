package com.onlinecode.itnhaque.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.service.ChapterService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ChapterController {
    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/chapter")
    @ApiMessage("Create a chapter")
    public ResponseEntity<Chapter> create(@Valid @RequestBody Chapter c) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.chapterService.create(c));
    }

    @GetMapping("/chapter/{id}")
    @ApiMessage("Fetch chapter by id")
    public ResponseEntity<Chapter> getById(@PathVariable("id") int id) throws IdInvalidException {
        Chapter chapter = this.chapterService.fetchById(id);
        if (chapter == null) {
            throw new IdInvalidException("Chapter với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(chapter);
    }
}
