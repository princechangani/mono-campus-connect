package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "otp_tokens")
@Data
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public enum OtpPurpose {
        EMAIL_VERIFICATION, PASSWORD_RESET
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public boolean isExpired() {
        return new Date().after(expiresAt);
    }
}

