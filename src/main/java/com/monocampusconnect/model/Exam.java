package com.monocampusconnect.model;

import jakarta.persistence.*;
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
    @Column(name = "exams_id")
    private Long examId;

    @Column(name = "exams_public_id", nullable = false, unique = true, updatable = false)
    private UUID examsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(nullable = false, unique = true)
    private String examCode;
    
    @Column(nullable = false)
    private String courseCode;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Date startDate;
    
    @Column(nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ExamType type;

    @ElementCollection
    private List<String> enrolledStudents;

    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Result> results;

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

    public enum ExamType {
        MIDTERM, FINAL, QUIZ, ASSIGNMENT
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
