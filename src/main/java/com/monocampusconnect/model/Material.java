package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "materials")
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "materials_id")
    private Long materialId;

    @Column(name = "materials_public_id", nullable = false, unique = true, updatable = false)
    private UUID materialsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String materialCode;
    private String courseCode;
    private String title;
    private String description;
    private String type; // PDF, DOC, PPT, VIDEO, LINK
    
    @Lob
    private byte[] fileContent;
    
    private String uploadedBy;
    private Date uploadedDate;
    private long fileSize;
    
    private int downloadCount;
    
    private Date lastUpdatedDate;
    private String fileType;

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
        return this.materialId;
    }

    public void setId(Long id) {
        this.materialId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (materialsPublicId == null) {
            materialsPublicId = UUID.randomUUID();
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
