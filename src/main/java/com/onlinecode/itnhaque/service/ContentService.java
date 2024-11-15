package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.repository.ContentRepository;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Content fetchById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            return optionalContent.get();
        }
        return null;
    }
}
