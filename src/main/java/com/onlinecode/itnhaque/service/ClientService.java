package com.onlinecode.itnhaque.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.domain.User;
import com.onlinecode.itnhaque.domain.response.ResCourseCardDTO;
import com.onlinecode.itnhaque.domain.response.ResLessonParameters;
import com.onlinecode.itnhaque.domain.response.ResUserDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.repository.CourseRepository;

@Service
public class ClientService {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final ContentService contentService;
    private final ChapterService chapterService;
    private final LessonService lessonService;

    public ClientService(CourseService courseService, CourseRepository courseRepository,
            ContentService contentService, ChapterService chapterService,
            LessonService lessonService) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.chapterService = chapterService;
        this.contentService = contentService;
        this.lessonService = lessonService;
    }

    public ResultPaginationDTO fetchActiveCourses(Specification<Course> spec, Pageable pageable) {
        Page<Course> pCourse = this.courseRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pCourse.getTotalPages());
        mt.setTotal(pCourse.getTotalElements());
        rs.setMeta(mt);
        List<Course> listCourses = pCourse.getContent();
        List<ResCourseCardDTO> listCoursesDTO = new ArrayList<>();
        for (Course course : listCourses) {
            ResCourseCardDTO cDTO = new ResCourseCardDTO();
            cDTO.setId(course.getId());
            cDTO.setTitle(course.getTitle());
            cDTO.setImage(course.getImage());
            cDTO.setDescription(course.getDescription());
            cDTO.setActive(course.isActive());
            cDTO.setCategory(course.getCategory().getName());
            cDTO.setSkill(course.getSkill().getName());
            cDTO.setFirstLessonId(course.getChapters().get(0).getLessons().get(0).getContentId());
            cDTO.setCreatedAt(course.getCreatedAt());
            cDTO.setCreatedBy(course.getCreatedBy());
            cDTO.setUpdatedAt(course.getUpdatedAt());
            cDTO.setUpdatedBy(course.getUpdatedBy());
            listCoursesDTO.add(cDTO);
        }

        rs.setResult(listCoursesDTO);
        return rs;
    }

    public Integer findNextLesson(int id) {
        Content content = this.contentService.fetchById(id);
        if (content != null) {
            try {
                Course course = this.courseService.fetchById(content.getCourseId());
                Chapter chapter = this.chapterService.fetchById(content.getChapterId());
                var totalChaptersInCourse = course.getChapters().size();
                var currentChapterIndex = chapter.getIndexInCourse();
                Lesson lesson = this.lessonService.fetchById(content.getLessonId());
                var totalLessonInCurrentChapter = chapter.getLessons().size();
                var currentLessonIndex = lesson.getIndexInChapter();
                if (totalChaptersInCourse == currentChapterIndex + 1
                        && totalLessonInCurrentChapter == currentLessonIndex + 1) {
                    return id;
                } else if (totalChaptersInCourse > currentChapterIndex + 1
                        && totalLessonInCurrentChapter == currentLessonIndex + 1) {
                    var chapters = course.getChapters();
                    for (Chapter c : chapters) {
                        if (c.getIndexInCourse() == currentChapterIndex + 1) {
                            return c.getLessons().get(0).getContentId();
                        }
                    }
                } else {
                    var lessons = chapter.getLessons();
                    for (Lesson l : lessons) {
                        // Xử lý từng chapter
                        if (l.getIndexInChapter() == currentLessonIndex + 1) {
                            return l.getContentId();
                        }
                    }
                }
            } catch (Exception e) {
                return id;
            }
        }
        return null;
    }

    public Integer findPreviousLesson(int id) {
        Content content = this.contentService.fetchById(id);
        if (content != null) {
            try {
                Course course = this.courseService.fetchById(content.getCourseId());
                Chapter chapter = this.chapterService.fetchById(content.getChapterId());
                var currentChapterIndex = chapter.getIndexInCourse();
                Lesson lesson = this.lessonService.fetchById(content.getLessonId());
                var currentLessonIndex = lesson.getIndexInChapter();
                if (currentChapterIndex == 0 && currentLessonIndex == 0) {
                    return id;
                } else if (currentChapterIndex > 0 && currentLessonIndex == 0) {
                    var chapters = course.getChapters();
                    for (Chapter c : chapters) {
                        if (c.getIndexInCourse() == currentChapterIndex - 1) {
                            var lessonSize = c.getLessons().size();
                            return c.getLessons().get(lessonSize - 1).getContentId();
                        }
                    }
                } else {
                    var lessons = chapter.getLessons();
                    for (Lesson l : lessons) {
                        // Xử lý từng chapter
                        if (l.getIndexInChapter() == currentLessonIndex - 1) {
                            return l.getContentId();
                        }
                    }
                }
            } catch (Exception e) {
                return id;
            }
        }
        return null;
    }

    public ResLessonParameters getLessonParameters(int contentId) {
        ResLessonParameters res = new ResLessonParameters();
        Content content = this.contentService.fetchById(contentId);
        if (content != null) {
            Course course = this.courseService.fetchById(content.getCourseId());
            res.setCourseChapterSize(course.getChapters().size());
            Chapter chapter = this.chapterService.fetchById(content.getChapterId());
            res.setChapterInCourseIndex(chapter.getIndexInCourse());
            Lesson lesson = this.lessonService.fetchById(content.getLessonId());
            res.setLessonInChapterIndex(lesson.getIndexInChapter());
            res.setChapterLessonSize(chapter.getLessons().size());
        }
        return res;
    }
}
