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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exams")
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "exam_public_id", nullable = false, unique = true, updatable = false)
    private UUID examsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "name", nullable = false)
    private String examCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false)
    private ExamType type;

    @Column(name = "semester_number", nullable = false)
    private Integer semesterNumber;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "total_marks", nullable = false)
    private Double totalMarks;

    @Column(name = "passing_marks", nullable = false)
    private Double passingMarks;

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

    // Legacy compatibility fields used by current service/controller contracts.
    @Transient
    private String title;

    @Transient
    private String description;

    @Transient
    private String courseCode;

    @Transient
    private List<String> enrolledStudents;

    @Transient
    private List<Result> results;

    public enum ExamType {
        MIDTERM, FINAL, QUIZ, ASSIGNMENT
    }

    public String getTitle() {
        return examCode;
    }

    public void setTitle(String title) {
        this.examCode = title;
    }

    public Long getId() {
        return this.examId;
    }

    public void setId(Long id) {
        this.examId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (examsPublicId == null) {
            examsPublicId = UUID.randomUUID();
        }
        Date now = new Date();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdateAudit() {
        updatedAt = new Date();
    }
}
