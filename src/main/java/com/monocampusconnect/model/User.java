package com.monocampusconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long userId;

    @Column(name = "users_public_id", nullable = false, unique = true, updatable = false)
    private UUID usersPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @Lob
    private byte[] profilePicture;

    private String department;
    private String semester;
    private String enrollmentNumber;
    private String facultyId;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private boolean enabled;

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
