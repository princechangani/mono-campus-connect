package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "result_details")
@Data
public class ResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_details_id")
    private Long resultDetailsId;

    @Column(name = "result_details_public_id", nullable = false, unique = true, updatable = false)
    private UUID resultDetailsPublicId;

    @ManyToOne
    @JoinColumn(name = "result_id")
    private Result result;

    private String subjectCode;
    private String subjectName;
    private double marksObtained;
    private double totalMarks;
    private String grade;

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
        return this.resultDetailsId;
    }

    public void setId(Long id) {
        this.resultDetailsId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (resultDetailsPublicId == null) {
            resultDetailsPublicId = UUID.randomUUID();
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
