package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "marks")
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id")
    private Long resultId;

    @Column(name = "mark_public_id", nullable = false, unique = true, updatable = false)
    private UUID resultsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "marks_obtained")
    private double obtainedMarks;

    @Column(name = "total_marks")
    private double totalMarks;

    @Column(name = "grade")
    private String grade;

    @Column(name = "grade_points")
    private Double gradePoints;

    @Column(name = "is_absent")
    private boolean absent;

    @Column(name = "is_withheld")
    private boolean withheld;

    @Column(name = "remarks")
    private String comments;

    @Column(name = "entered_by")
    private Long enteredBy;

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

    // Legacy compatibility fields for existing service contracts.
    @Transient
    private String examCode;

    @Transient
    private String courseCode;

    @Transient
    private Exam exam;

    @Transient
    private List<ResultDetail> resultDetails;

    @Transient
    private String status;

    @Transient
    private Date resultDate;

    public Long getId() {
        return this.resultId;
    }

    public void setId(Long id) {
        this.resultId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (resultsPublicId == null) {
            resultsPublicId = UUID.randomUUID();
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
