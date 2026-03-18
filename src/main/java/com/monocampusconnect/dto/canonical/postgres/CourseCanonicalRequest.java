package com.monocampusconnect.dto.canonical.postgres;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseCanonicalRequest {
    @NotNull
    private UUID tenantId;

    @NotNull
    private Long departmentId;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private String description;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer credits;

    private Integer lectureHoursPerWeek;
    private Integer labHoursPerWeek;
    private Integer tutorialHoursPerWeek;
    private String courseType;
    private Boolean active;
}

