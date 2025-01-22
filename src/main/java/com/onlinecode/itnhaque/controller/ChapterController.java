package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.service.ChapterService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class ChapterController {
    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/chapter")
    @ApiMessage("Create a chapter")
    public ResponseEntity<Content> create(@Valid @RequestBody Chapter c) throws IdInvalidException {
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

    @PutMapping("/chapter")
    @ApiMessage("Update a chapter")
    public ResponseEntity<Chapter> update(@Valid @RequestBody Chapter c) throws IdInvalidException {
        Chapter chapterDB = this.chapterService.fetchById(c.getId());
        if (chapterDB == null) {
            throw new IdInvalidException("Chapter với id = " + c.getId() + " không tồn tại");
        }
        chapterDB.setTitle(c.getTitle());
        return ResponseEntity.ok().body(this.chapterService.update(chapterDB));
    }

    @DeleteMapping("/chapter/{id}")
    @ApiMessage("delete a chapter")
    public ResponseEntity<Integer> delete(@PathVariable("id") int id) throws IdInvalidException {
        Chapter chapterDB = this.chapterService.fetchById(id);
        if (chapterDB == null) {
            throw new IdInvalidException("Chapter với id = " + id + " không tồn tại");
        }
        if (chapterDB.getCourse().getChapters().size() < 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        List<Lesson> lessons = chapterDB.getLessons();
        if (lessons.size() > 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok().body(this.chapterService.delete(id));
    }
}
