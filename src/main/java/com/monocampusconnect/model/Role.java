package com.monocampusconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_public_id", nullable = false, unique = true, updatable = false)
    private UUID rolesPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName code;

    @Column(name = "description")
    private String name;

    @Column(name = "is_system", nullable = false)
    private boolean isSystem = false;

    @Column(name = "created_at", nullable = false)
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

    public Role(RoleName code, String name) {
        this.code = code;
        this.name = name;
    }

    @PrePersist
    public void onCreate() {
        Date now = new Date();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
        if (rolesPublicId == null) {
            rolesPublicId = UUID.randomUUID();
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

    public Long getId() {
        return this.roleId;
    }

    public void setId(Long id) {
        this.roleId = id;
    }

    public enum RoleName {
        SUPER_ADMIN,
        ADMIN,
        HOD,
        FACULTY,
        STUDENT,
        PARENT,
        WARDEN
    }
}
