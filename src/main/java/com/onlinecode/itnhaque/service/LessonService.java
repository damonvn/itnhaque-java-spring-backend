package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.domain.request.ReqUpdateLessonVideo;
import com.onlinecode.itnhaque.repository.ContentRepository;
import com.onlinecode.itnhaque.repository.LessonRepository;

@Service
public class LessonService {
    private final ChapterService chapterService;
    private final ContentService contentService;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    public LessonService(ChapterService chapterService, LessonRepository lessonRepository,
            ContentRepository contentRepository, ContentService contentService) {
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
        this.chapterService = chapterService;
        this.contentService = contentService;
    }

    public Content create(Lesson l) {
        Chapter chapterDB = chapterService.fetchById(l.getChapter().getId());
        if (chapterDB.getLessons().size() > l.getIndexInChapter()) {
            List<Lesson> lessons = chapterDB.getLessons();
            lessons.forEach(lesson -> {
                if (lesson.getIndexInChapter() >= l.getIndexInChapter()) {
                    lesson.setIndexInChapter(lesson.getIndexInChapter() + 1);
                    this.lessonRepository.save(lesson);
                }
            });
        }

        l.setChapter(chapterDB);
        Lesson lessonDB = this.lessonRepository.save(l);

        // create content
        Content content = new Content();
        content.setTitle(lessonDB.getTitle());
        content.setContent("Lesson content");
        content.setCourseId(lessonDB.getCourseId());
        content.setChapterId(chapterDB.getId());
        content.setLessonId(lessonDB.getId());
        Content contentDB = this.contentRepository.save(content);

        // update lesson contentId
        lessonDB.setContentId(contentDB.getId());
        this.lessonRepository.save(lessonDB);

        return contentDB;
    }

    public Content handleUpdateLessonContent(Content content) {
        Lesson lessonDB = this.fetchById(content.getLessonId());
        if (lessonDB != null) {
            lessonDB.setTitle(content.getTitle());
            lessonDB.setLinkVideo(content.getLessonVideoURL());
            this.lessonRepository.save(lessonDB);
        }
        Content contentDB = this.contentRepository.save(content);
        return contentDB;
    }

    public Content handleUpdateLessonVideo(ReqUpdateLessonVideo req) {
        Lesson lessonDB = fetchById(req.getLessonId());
        lessonDB.setLinkVideo(req.getLessonVideoURL());
        this.lessonRepository.save(lessonDB);

        Content contentDB = this.contentService.fetchById(lessonDB.getContentId());
        contentDB.setLessonVideoURL(req.getLessonVideoURL());
        Content resContent = this.contentRepository.save(contentDB);
        return resContent;
    }

    public Lesson fetchById(int id) {
        Optional<Lesson> optionalLesson = this.lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            return optionalLesson.get();
        }
        return null;
    }
}
