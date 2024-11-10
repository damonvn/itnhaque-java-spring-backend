package com.onlinecode.itnhaque.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.repository.ContentRepository;
import com.onlinecode.itnhaque.repository.LessonRepository;

@Service
public class LessonService {
    private final ChapterService chapterService;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    public LessonService(ChapterService chapterService, LessonRepository lessonRepository,
            ContentRepository contentRepository) {
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
        this.chapterService = chapterService;
    }

    public Lesson create(Lesson l) {
        Chapter chapterDB = chapterService.fetchById(l.getChapter().getId());
        l.setChapter(chapterDB);

        // create content
        Content content = new Content();
        content.setCourseId(l.getCourseId());
        content.setTitle(l.getTitle());
        Content contentDB = this.contentRepository.save(content);
        l.setContentId(contentDB.getId());
        Lesson lessonDB = this.lessonRepository.save(l);
        return lessonDB;
    }

    public Lesson fetchById(Integer id) {
        Optional<Lesson> optionalLesson = this.lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            return optionalLesson.get();
        }
        return null;
    }
}
