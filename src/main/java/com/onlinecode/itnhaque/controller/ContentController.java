package com.onlinecode.itnhaque.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.service.ContentService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/content/{id}")
    @ApiMessage("Fetch content by id")
    public ResponseEntity<Content> getById(@PathVariable("id") int id) throws IdInvalidException {
        Content content = this.contentService.fetchById(id);
        if (content == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(content);
    }
}
