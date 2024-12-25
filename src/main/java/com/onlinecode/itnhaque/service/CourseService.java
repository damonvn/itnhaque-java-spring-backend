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
        chapter.setTitle("Chapter Title");
        chapter.setCourse(courseDB);
        chapter.setIndexInCourse(0);
        Chapter chapterDB = this.chapterRepository.save(chapter);

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

        // return fetchById(courseDB.getId());
        return courseDB;
    }

    public Course fetchById(int id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        }
        return null;
    }

    public Course clientFetchById(int id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course courseDB = courseOptional.get();
            if (!courseDB.isActive()) {
                return courseDB;
            }
        }
        return null;
    }
}
