package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.repository.LessonContentRepository;

@Service
public class LessonContentService {

    private final LessonContentRepository lessonContentRepository;

    public LessonContentService(LessonContentRepository lessonContentRepository) {
        this.lessonContentRepository = lessonContentRepository;
    }

    public Content create(Content l) {
        return this.lessonContentRepository.save(l);
    }

    public Content fetchById(int id) {
        Optional<Content> roleOptional = this.lessonContentRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

}
