package com.onlinecode.itnhaque.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResContentDTO {
    private int id;
    private int courseId;
    private int chapterId;
    private int lessonId;
    private String courseTitle;
    private String lessonVideoURL;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
