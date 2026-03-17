package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tenants_id")
    private UUID tenantId;

    @Column(name = "tenants_public_id", nullable = false, unique = true, updatable = false)
    private UUID tenantsPublicId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code; // short unique identifier e.g. "MIT", "ABC_COLLEGE"

    private String logoUrl;
    private String timezone; // e.g., "Asia/Kolkata"
    private String contactEmail;
    private String contactPhone;
    private String address;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public enum SubscriptionPlan {
        BASIC, PRO, ENTERPRISE
    }

    public UUID getId() {
        return this.tenantId;
    }

    public void setId(UUID id) {
        this.tenantId = id;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        if (tenantsPublicId == null) {
            tenantsPublicId = UUID.randomUUID();
        }
        if (!enabled && createdAt == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
