package com.onlinecode.itnhaque.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResLessonParameters {
    private int LessonInChapterIndex;
    private int ChapterInCourseIndex;
    private int CourseChapterSize;
    private int ChapterLessonSize;
}
