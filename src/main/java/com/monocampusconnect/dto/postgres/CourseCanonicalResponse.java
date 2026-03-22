package com.monocampusconnect.dto.postgres;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CourseCanonicalResponse {
    private Long courseId;
    private UUID coursePublicId;
    private UUID tenantId;
    private Long departmentId;
    private String name;
    private String code;
    private String description;
    private Integer credits;
    private Integer lectureHoursPerWeek;
    private Integer labHoursPerWeek;
    private Integer tutorialHoursPerWeek;
    private String courseType;
    private Boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

