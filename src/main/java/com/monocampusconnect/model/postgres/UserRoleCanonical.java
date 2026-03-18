package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_roles")
public class UserRoleCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @Column(name = "user_role_public_id")
    private UUID userRolePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "context_type")
    private String contextType;

    @Column(name = "context_id")
    private Long contextId;

    @Column(name = "assigned_at")
    private OffsetDateTime assignedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}

