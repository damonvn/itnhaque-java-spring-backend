package com.onlinecode.itnhaque.service;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.repository.ChapterRepository;
import com.onlinecode.itnhaque.repository.ContentRepository;
import com.onlinecode.itnhaque.repository.CourseRepository;
import com.onlinecode.itnhaque.repository.LessonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
            LessonRepository lessonRepository, ContentRepository contentRepository) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
    }

    public Course create(Course c) {
        Course courseDB = this.courseRepository.save(c);

        // create chapter
        Chapter chapter = new Chapter();
        chapter.setTitle("Default Chapter Title");
        chapter.setCourse(courseDB);
        Chapter chapterDB = this.chapterRepository.save(chapter);

        // create content
        Content content = new Content();
        content.setTitle("Default Lesson Title");
        content.setCourseId(courseDB.getId());
        Content contentDB = this.contentRepository.save(content);

        // create lesson
        Lesson lesson = new Lesson();
        lesson.setTitle(contentDB.getTitle());
        lesson.setContentId(contentDB.getId());
        lesson.setChapter(chapterDB);
        this.lessonRepository.save(lesson);

        return fetchById(courseDB.getId());
    }

    public Course fetchById(int id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        }
        return null;
    }

}
