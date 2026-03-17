package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "results")
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "results_id")
    private Long resultId;

    @Column(name = "results_public_id", nullable = false, unique = true, updatable = false)
    private UUID resultsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private Long studentId;
    private String examCode;
    private String courseCode;


    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    private List<ResultDetail> resultDetails;
    private double totalMarks;
    private double obtainedMarks;
    private String grade;
    private String status; 
    private String comments;
    private Date resultDate;

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
