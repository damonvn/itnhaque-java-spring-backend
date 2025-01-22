package com.onlinecode.itnhaque.service;

import java.util.Optional;
import java.util.List;
import java.util.ListResourceBundle;

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

    public Content create(Chapter c) {
        Course courseDB = this.courseService.fetchById(c.getCourse().getId());
        if (courseDB.getChapters().size() > c.getIndexInCourse()) {
            List<Chapter> chapters = courseDB.getChapters();
            chapters.forEach(chapter -> {
                if (chapter.getIndexInCourse() >= c.getIndexInCourse()) {
                    chapter.setIndexInCourse(chapter.getIndexInCourse() + 1);
                    this.chapterRepository.save(chapter);
                }
            });
        }
        c.setCourse(courseDB);

        // create chapter
        Chapter chapterDB = this.chapterRepository.save(c);

        // create lesson
        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson title");
        lesson.setCourseId(courseDB.getId());
        lesson.setChapter(chapterDB);
        lesson.setIndexInChapter(0);
        Lesson lessonDB = this.lessonRepository.save(lesson);

        // create content
        Content content = new Content();
        content.setTitle(lessonDB.getTitle());
        content.setContent("Lesson content");
        content.setCourseId(courseDB.getId());
        content.setChapterId(chapterDB.getId());
        content.setLessonId(lessonDB.getId());
        Content contentDB = this.contentRepository.save(content);

        // update lesson contentId
        lessonDB.setContentId(contentDB.getId());
        this.lessonRepository.save(lessonDB);

        return contentDB;
    }

    public Chapter fetchById(int id) {
        Optional<Chapter> chapterOptional = this.chapterRepository.findById(id);
        if (chapterOptional.isPresent()) {
            return chapterOptional.get();
        }
        return null;
    }

    public Chapter update(Chapter chapter) {
        return this.chapterRepository.save(chapter);
    }

    public Integer delete(int id) {
        Chapter chapter = this.fetchById(id);
        List<Lesson> lessons = chapter.getLessons();

        Chapter resChapter = chapter.getIndexInCourse() == 0 ? this.chapterRepository.findByIndexInCourse(1)
                : this.chapterRepository.findByIndexInCourse(chapter.getIndexInCourse() - 1);
        List<Chapter> chapters = chapter.getCourse().getChapters();

        this.lessonRepository.deleteById(lessons.get(0).getId());
        this.chapterRepository.deleteById(id);
        for (Chapter chap : chapters) {
            if (chap.getIndexInCourse() > chapter.getIndexInCourse()) {
                chap.setIndexInCourse(chap.getIndexInCourse() - 1);
                this.chapterRepository.save(chap);
            }
        }
        return resChapter.getLessons().get(0).getContentId();
    }
}
