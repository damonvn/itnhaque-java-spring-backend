package com.onlinecode.itnhaque.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.domain.response.ResCourseDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
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
    private final SkillService skillService;
    private final CategoryService categoryService;

    public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
            LessonRepository lessonRepository, ContentRepository contentRepository,
            SkillService skillService, CategoryService categoryService) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
        this.skillService = skillService;
        this.categoryService = categoryService;
    }

    public Course create(Course c) {
        // check category
        if (c.getSkill() != null) {
            Category category = this.categoryService.fetchById(c.getCategory().getId());
            c.setCategory(category != null ? category : null);
        }

        // check skill
        if (c.getSkill() != null) {
            Skill skill = this.skillService.fetchById(c.getSkill().getId());
            c.setSkill(skill != null ? skill : null);
        }

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

    public ResCourseDTO fetchUpdateCourseById(int id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            ResCourseDTO courseDTO = new ResCourseDTO();
            Course courseEntity = courseOptional.get();
            courseDTO.setId(courseEntity.getId());
            courseDTO.setTitle(courseEntity.getTitle());
            courseDTO.setImage(courseEntity.getImage());
            courseDTO.setDescription(courseEntity.getDescription());
            courseDTO.setActive(courseEntity.isActive());
            // set Category
            ResCourseDTO.CourseCategory category = new ResCourseDTO.CourseCategory();
            category.setId(courseEntity.getCategory().getId());
            category.setValue(courseEntity.getCategory().getValue());
            category.setLabel(courseEntity.getCategory().getName());
            courseDTO.setCategory(category);
            // set skill
            ResCourseDTO.CourseSkill skill = new ResCourseDTO.CourseSkill();
            skill.setId(courseEntity.getSkill().getId());
            skill.setValue(courseEntity.getSkill().getValue());
            skill.setLabel(courseEntity.getSkill().getName());
            courseDTO.setSkill(skill);
            courseDTO.setCreatedAt(courseEntity.getCreatedAt());
            courseDTO.setCreatedBy(courseEntity.getCreatedBy());
            courseDTO.setUpdatedAt(courseEntity.getUpdatedAt());
            courseDTO.setUpdatedBy(courseEntity.getUpdatedBy());
            return courseDTO;
        }
        return null;
    }

    public Course clientFetchById(int id) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course courseDB = courseOptional.get();
            if (courseDB.isActive()) {
                return courseDB;
            }
        }
        return null;
    }

    public ResultPaginationDTO getCourses(Specification<Course> spec, Pageable pageable) {
        Page<Course> pCourse = this.courseRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCourse.getTotalPages());
        mt.setTotal(pCourse.getTotalElements());

        rs.setMeta(mt);

        List<ResCourseDTO> resultList = new ArrayList<>();
        for (Course course : pCourse.getContent()) {
            ResCourseDTO resCourse = new ResCourseDTO();

            resCourse.setId(course.getId());
            resCourse.setTitle(course.getTitle());
            resCourse.setImage(course.getImage());
            resCourse.setDescription(course.getDescription());
            resCourse.setActive(course.isActive());
            resCourse.setCreatedAt(course.getCreatedAt());
            resCourse.setCreatedBy(course.getCreatedBy());
            resCourse.setUpdatedAt(course.getUpdatedAt());
            resCourse.setUpdatedBy(course.getUpdatedBy());
            resultList.add(resCourse);
        }

        rs.setResult(resultList);
        return rs;
    }

    public ResCourseDTO update(Course c) {
        Course courseDB = this.fetchById(c.getId());
        courseDB.setTitle(c.getTitle());
        courseDB.setDescription(c.getDescription());
        courseDB.setImage(c.getImage());

        this.courseRepository.save(courseDB);
        ResCourseDTO courseDTO = new ResCourseDTO();
        courseDTO.setId(courseDB.getId());
        courseDTO.setTitle(courseDB.getTitle());
        courseDTO.setDescription(courseDB.getDescription());
        courseDTO.setImage(courseDB.getImage());
        courseDTO.setActive(courseDB.isActive());
        courseDTO.setCreatedAt(courseDB.getCreatedAt());
        courseDTO.setCreatedBy(courseDB.getCreatedBy());
        courseDTO.setUpdatedAt(courseDB.getUpdatedAt());
        courseDTO.setUpdatedBy(courseDB.getUpdatedBy());
        return courseDTO;
    }

    public Void updateCourseActive(Course c) {
        Course courseDB = this.fetchById(c.getId());
        courseDB.setActive(c.isActive());
        this.courseRepository.save(courseDB);
        return null;
    }
}
