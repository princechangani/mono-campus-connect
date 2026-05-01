package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tenants")
public class TenantCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "tenant_public_id")
    private UUID tenantPublicId;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "domain")
    private String domain;

    @Column(name = "plan")
    private String plan;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "locale")
    private String locale;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "max_faculty")
    private Integer maxFaculty;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "subscription_expires_at")
    private OffsetDateTime subscriptionExpiresAt;
}

