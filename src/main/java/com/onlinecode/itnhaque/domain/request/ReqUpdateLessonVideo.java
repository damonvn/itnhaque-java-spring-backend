package com.onlinecode.itnhaque.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateLessonVideo {
    private int lessonId;
    private String lessonVideoURL;
}
