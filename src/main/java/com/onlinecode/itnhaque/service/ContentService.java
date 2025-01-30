package com.onlinecode.itnhaque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Content;
import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.response.ResContentDTO;
import com.onlinecode.itnhaque.repository.ContentRepository;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final CourseService courseService;

    public ContentService(ContentRepository contentRepository, CourseService courseService) {
        this.contentRepository = contentRepository;
        this.courseService = courseService;
    }

    public Content fetchById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            return optionalContent.get();
        }
        return null;
    }

    public ResContentDTO fetchAdminContentById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            ResContentDTO contentDTO = new ResContentDTO();
            Content content = optionalContent.get();
            Course course = this.courseService.fetchById(content.getCourseId());
            contentDTO.setId(content.getId());
            contentDTO.setCourseId(content.getCourseId());
            contentDTO.setCourseTitle(course.getTitle());
            contentDTO.setChapterId(content.getChapterId());
            contentDTO.setLessonId(content.getLessonId());
            contentDTO.setLessonVideoURL(content.getLessonVideoURL());
            contentDTO.setTitle(content.getTitle());
            contentDTO.setContent(content.getContent());
            contentDTO.setCreatedAt(content.getCreatedAt());
            contentDTO.setUpdatedAt(content.getUpdatedAt());
            contentDTO.setCreatedBy(content.getCreatedBy());
            contentDTO.setUpdatedBy(content.getUpdatedBy());
            contentDTO.setCourseTitle(course.getTitle());
            return contentDTO;
        }
        return null;
    }

    public ResContentDTO fetchClientResContentDTOById(int id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent() == false)
            return null;
        Content content = optionalContent.get();
        Course course = this.courseService.fetchById(content.getCourseId());
        if (course == null || course.isActive() == false)
            return null;
        ResContentDTO contentDTO = new ResContentDTO();
        contentDTO.setId(content.getId());
        contentDTO.setCourseId(content.getCourseId());
        contentDTO.setCourseTitle(course.getTitle());
        contentDTO.setChapterId(content.getChapterId());
        contentDTO.setLessonId(content.getLessonId());
        contentDTO.setLessonVideoURL(content.getLessonVideoURL());
        contentDTO.setTitle(content.getTitle());
        contentDTO.setContent(content.getContent());
        contentDTO.setCreatedAt(content.getCreatedAt());
        contentDTO.setUpdatedAt(content.getUpdatedAt());
        contentDTO.setCreatedBy(content.getCreatedBy());
        contentDTO.setUpdatedBy(content.getUpdatedBy());
        contentDTO.setCourseTitle(course.getTitle());
        return contentDTO;
    }

    public Content clientFetchById(Integer id) {
        Optional<Content> optionalContent = this.contentRepository.findById(id);
        if (optionalContent.isPresent()) {
            Content contentDB = optionalContent.get();
            Course courseDB = this.courseService.fetchById(contentDB.getCourseId());
            if (courseDB != null) {
                if (!courseDB.isActive()) {
                    return contentDB;
                }
            }
        }
        return null;
    }
}
