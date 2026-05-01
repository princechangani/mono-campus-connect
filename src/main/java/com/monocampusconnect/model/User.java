package com.monocampusconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_public_id", nullable = false, unique = true, updatable = false)
    private UUID usersPublicId;

    // Tenant context currently uses UUID in the app; kept for compatibility.
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "address_line1")
    private String address;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "is_active")
    private boolean enabled;

    @Column(name = "email_verified_at")
    private Date emailVerifiedAt;

    @Column(name = "phone_verified_at")
    private Date phoneVerifiedAt;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "locale")
    private String locale = "en";

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "last_login_at")
    private Date lastLoginAt;

    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled = false;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private Date passwordResetExpiresAt;

    @JsonIgnore
    @Lob
    @Transient
    private byte[] profilePicture;

    @Transient
    private String department;

    @Transient
    private String semester;

    @Transient
    private String enrollmentNumber;

    @Transient
    private String facultyId;

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

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public Long getId() {
        return this.userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (usersPublicId == null) {
            usersPublicId = UUID.randomUUID();
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
