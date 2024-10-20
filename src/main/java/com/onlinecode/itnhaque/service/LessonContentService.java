package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.LessonContent;
import com.onlinecode.itnhaque.domain.Role;
import com.onlinecode.itnhaque.repository.LessonContentRepository;

@Service
public class LessonContentService {

    private final LessonContentRepository lessonContentRepository;

    public LessonContentService(LessonContentRepository lessonContentRepository) {
        this.lessonContentRepository = lessonContentRepository;
    }

    public LessonContent create(LessonContent l) {
        return this.lessonContentRepository.save(l);
    }

    public LessonContent fetchById(long id) {
        Optional<LessonContent> roleOptional = this.lessonContentRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

}
