package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(
    name = "user_roles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "user_id", "role_id", "context_type", "context_id"})
)
@Data
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @Column(name = "user_role_public_id", nullable = false, unique = true, updatable = false)
    private UUID userRolesPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "context_type")
    private String contextType;

    @Column(name = "context_id")
    private Long contextId;

    @Column(name = "assigned_at")
    private Date assignedAt;

    @Column(name = "expires_at")
    private Date expiresAt;

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

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.tenantId = user.getTenantId();
        this.assignedAt = new Date();
        if (this.userRolesPublicId == null) {
            this.userRolesPublicId = UUID.randomUUID();
        }
    }

    public Long getId() {
        return this.userRoleId;
    }

    public void setId(Long id) {
        this.userRoleId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (userRolesPublicId == null) {
            userRolesPublicId = UUID.randomUUID();
        }
        Date now = new Date();
        if (assignedAt == null) {
            assignedAt = now;
        }
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
