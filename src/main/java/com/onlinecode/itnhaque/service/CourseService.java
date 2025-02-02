package com.onlinecode.itnhaque.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Category;
import com.onlinecode.itnhaque.domain.Chapter;
import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Filestore;
import com.onlinecode.itnhaque.domain.Lesson;
import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.domain.response.ResCourseCardDTO;
import com.onlinecode.itnhaque.domain.response.ResCourseDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.repository.ChapterRepository;
import com.onlinecode.itnhaque.repository.ContentRepository;
import com.onlinecode.itnhaque.repository.CourseRepository;
import com.onlinecode.itnhaque.repository.FilestoreRepository;
import com.onlinecode.itnhaque.repository.LessonRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final FilestoreRepository filestoreRepository;
    private final FileService fileService;

    @Value("${upload-file.base-uri}")
    private String baseURI;

    public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
            LessonRepository lessonRepository, ContentRepository contentRepository,
            SkillService skillService, CategoryService categoryService,
            FilestoreRepository filestoreRepository, FileService fileService) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.contentRepository = contentRepository;
        this.skillService = skillService;
        this.categoryService = categoryService;
        this.filestoreRepository = filestoreRepository;
        this.fileService = fileService;
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

        List<Course> listCourses = pCourse.getContent();
        List<ResCourseCardDTO> resultList = new ArrayList<>();
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
            resultList.add(cDTO);
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

    public Void delete(Course course) {
        this.contentRepository.deleteById(course.getChapters().get(0).getLessons().get(0).getContentId());
        this.lessonRepository.deleteById(course.getChapters().get(0).getLessons().get(0).getId());
        this.chapterRepository.deleteById(course.getChapters().get(0).getId());
        this.courseRepository.deleteById(course.getId());
        Optional<Filestore> file = this.filestoreRepository.findByName(course.getImage());
        if (file.isPresent()) {
            this.filestoreRepository.deleteById(file.get().getId());
            try {
                URI uri = new URI(baseURI + file.get().getFolder() + "/" + course.getImage());
                Path path = Paths.get(uri);

                // Xóa file trên hệ thống file
                Files.delete(path);
                System.out.println(">>> File deleted successfully, PATH = " + path);
            } catch (NoSuchFileException e) {
                System.err.println(">>> No such file exists to delete: " + e.getMessage());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
