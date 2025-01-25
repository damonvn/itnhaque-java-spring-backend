package com.onlinecode.itnhaque.domain.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCourseCardDTO {
    private int id;
    private String title;
    private String image;
    private String description;
    private boolean active;
    private String category;
    private String skill;
    private int firstLessonId;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
}
