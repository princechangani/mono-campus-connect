package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "permissions")
public class Permission extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_public_id")
    private UUID permissionPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "module")
    private String module;

    @Column(name = "action")
    private String action;

    @Column(name = "resource")
    private String resource;

    @Column(name = "description")
    private String description;
}

