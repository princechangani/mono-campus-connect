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
    private UUID id;

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
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public enum SubscriptionPlan {
        BASIC, PRO, ENTERPRISE
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        if (enabled == false && createdAt == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}

