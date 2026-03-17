package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "otp_tokens")
@Data
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_tokens_id")
    private Long otpTokenId;

    @Column(name = "otp_tokens_public_id", nullable = false, unique = true, updatable = false)
    private UUID otpTokensPublicId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;

    private boolean used;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public enum OtpPurpose {
        EMAIL_VERIFICATION, PASSWORD_RESET
    }

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        createdAt = now;
        updatedAt = now;
        if (otpTokensPublicId == null) {
            otpTokensPublicId = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdateAudit() {
        updatedAt = new Date();
    }

    public Long getId() {
        return this.otpTokenId;
    }

    public void setId(Long id) {
        this.otpTokenId = id;
    }

    public boolean isExpired() {
        return new Date().after(expiresAt);
    }
}
