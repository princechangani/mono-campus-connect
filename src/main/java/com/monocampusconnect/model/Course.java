package com.monocampusconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_public_id", nullable = false, unique = true, updatable = false)
    private UUID coursesPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "code")
    private String courseCode;

    @Column(name = "name")
    private String courseName;

    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private int credits;

    @Column(name = "lecture_hours_per_week")
    private Integer lectureHoursPerWeek = 3;

    @Column(name = "lab_hours_per_week")
    private Integer labHoursPerWeek = 0;

    @Column(name = "tutorial_hours_per_week")
    private Integer tutorialHoursPerWeek = 0;

    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType = SubjectType.COMPULSORY;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Legacy compatibility fields retained for current API contracts.
    @Transient
    private String department;

    @Transient
    private String instructor;

    @Transient
    private String facultyId;

    @Transient
    private String semester;

    @Transient
    private String category;

    public enum SubjectType {
        COMPULSORY, OPTIONAL
    }

    public Long getId() {
        return this.courseId;
    }

    public void setId(Long id) {
        this.courseId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (coursesPublicId == null) {
            coursesPublicId = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = new Date();
        }
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdateAudit() {
        updatedAt = new Date();
    }
}
