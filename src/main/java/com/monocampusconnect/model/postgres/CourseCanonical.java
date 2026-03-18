package com.monocampusconnect.model.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "courses")
public class CourseCanonical extends BaseAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_public_id")
    private UUID coursePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "lecture_hours_per_week")
    private Integer lectureHoursPerWeek;

    @Column(name = "lab_hours_per_week")
    private Integer labHoursPerWeek;

    @Column(name = "tutorial_hours_per_week")
    private Integer tutorialHoursPerWeek;

    @Column(name = "course_type")
    private String courseType;

    @Column(name = "is_active")
    private Boolean isActive;
}
