package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.repository.ContentRepository;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final CourseService courseService;

    public ContentService(ContentRepository contentRepository, CourseService courseService) {
        this.contentRepository = contentRepository;
        this.courseService = courseService;
    }

    public Content fetchById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            return optionalContent.get();
        }
        return null;
    }

    public Content clientFetchById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            Content contentDB = optionalContent.get();
            Course courseDB = this.courseService.fetchById(contentDB.getCourseId());
            if (courseDB != null) {
                if (!courseDB.isActive()) {
                    return contentDB;
                }
            }
        }
        return null;
    }
}
