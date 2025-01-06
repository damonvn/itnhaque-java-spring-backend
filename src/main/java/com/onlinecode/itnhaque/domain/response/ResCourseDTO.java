package com.onlinecode.itnhaque.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResCourseDTO {
    private int id;
    private String title;
    private String image;
    private String description;
    private boolean active;
    private CourseCategory category;
    private CourseSkill skill;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseCategory {
        private int id;
        private String value;
        private String label;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseSkill {
        private int id;
        private String value;
        private String label;
    }
}
