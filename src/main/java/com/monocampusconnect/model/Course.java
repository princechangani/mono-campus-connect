package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courses_id")
    private Long courseId;

    @Column(name = "courses_public_id", nullable = false, unique = true, updatable = false)
    private UUID coursesPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String courseCode;
    private String courseName;
    private String department;
    private int credits;
    private String instructor;   // faculty name
    private String facultyId;    // faculty user id / FAC001 etc.
    private String semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type")
    private SubjectType subjectType = SubjectType.COMPULSORY;

    private String category;     // e.g. "Core", "Lab", "Elective", "Project"

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
