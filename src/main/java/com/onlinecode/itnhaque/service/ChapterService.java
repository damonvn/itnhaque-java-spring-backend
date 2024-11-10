package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.repository.ChapterRepository;
import com.onlinecode.itnhaque.repository.ContentRepository;
import com.onlinecode.itnhaque.repository.LessonRepository;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseService courseService;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    public ChapterService(ChapterRepository chapterRepository, CourseService courseService,
            LessonRepository lessonRepository, ContentRepository contentRepository) {
        this.chapterRepository = chapterRepository;
        this.courseService = courseService;
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
    }

    public Chapter create(Chapter c) {
        Course courseDB = this.courseService.fetchById(c.getCourse().getId());
        c.setCourse(courseDB);
        Chapter chapterDB = this.chapterRepository.save(c);

        // create content
        Content content = new Content();
        content.setTitle("Default Lesson Title");
        content.setCourseId(c.getCourse().getId());
        Content contentDB = this.contentRepository.save(content);

        // create lesson
        Lesson lesson = new Lesson();
        lesson.setTitle(contentDB.getTitle());
        lesson.setContentId(contentDB.getId());
        lesson.setChapter(chapterDB);
        this.lessonRepository.save(lesson);
        return chapterDB;
    }

    public Chapter fetchById(int id) {
        Optional<Chapter> chapterOptional = this.chapterRepository.findById(id);
        if (chapterOptional.isPresent()) {
            return chapterOptional.get();
        }
        return null;
    }

}
