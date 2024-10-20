package com.onlinecode.itnhaque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

import com.onlinecode.itnhaque.util.SecurityUtil;

@Entity
@Table(name = "lesson_contents")
@Getter
@Setter
public class LessonContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Sử dụng cùng ID với Lesson

    @NotBlank(message = "Course không được để trống")
    private long courseId;

    @NotBlank(message = "title không được để trống")
    private String title;

    @NotBlank(message = "title không được để trống")
    @Column(columnDefinition = "TEXT")
    private String content; // Đây là nội dung của bài học

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }
}
