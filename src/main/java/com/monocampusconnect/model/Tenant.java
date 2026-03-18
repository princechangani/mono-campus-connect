package com.monocampusconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {

    // App still uses UUID tenant id in APIs/JWT; mapped to schema public_id.
    @Id
    @Column(name = "tenant_public_id", nullable = false, unique = true, updatable = false)
    private UUID tenantId;

    @Column(name = "tenant_id", insertable = false, updatable = false)
    private Long tenantRowId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String code;

    @Column(name = "domain")
    private String domain;

    @Column(name = "plan", nullable = false)
    private String subscriptionPlanCode = "basic";

    @Column(name = "timezone", nullable = false)
    private String timezone = "UTC";

    @Column(name = "locale", nullable = false)
    private String locale = "en";

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents = 5000;

    @Column(name = "max_faculty", nullable = false)
    private Integer maxFaculty = 500;

    @Column(name = "is_active", nullable = false)
    private boolean enabled = true;

    @Column(name = "subscription_expires_at")
    private Date subscriptionExpiresAt;

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

    // Legacy request fields retained as non-persisted compatibility properties.
    @Transient
    private String contactEmail;

    @Transient
    private String contactPhone;

    @Transient
    private String address;

    public enum SubscriptionPlan {
        BASIC, PRO, ENTERPRISE
    }

    public UUID getTenantsPublicId() {
        return tenantId;
    }

    public void setTenantsPublicId(UUID tenantsPublicId) {
        this.tenantId = tenantsPublicId;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        try {
            return SubscriptionPlan.valueOf(subscriptionPlanCode.toUpperCase());
        } catch (Exception ignored) {
            return SubscriptionPlan.BASIC;
        }
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlanCode = subscriptionPlan == null
                ? SubscriptionPlan.BASIC.name().toLowerCase()
                : subscriptionPlan.name().toLowerCase();
    }

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
        if (tenantId == null) {
            tenantId = UUID.randomUUID();
        }
        if (timezone == null || timezone.isBlank()) {
            timezone = "UTC";
        }
        if (locale == null || locale.isBlank()) {
            locale = "en";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
