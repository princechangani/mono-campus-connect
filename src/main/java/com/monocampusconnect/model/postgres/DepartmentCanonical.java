package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "departments")
public class DepartmentCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_public_id")
    private UUID departmentPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "head_faculty_id")
    private Long headFacultyId;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @Column(name = "description")
    private String description;
}

