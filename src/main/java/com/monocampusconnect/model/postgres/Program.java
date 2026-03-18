package com.monocampusconnect.model.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "programs")
public class Program extends BaseAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "program_public_id")
    private UUID programPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "degree_level")
    private String degreeLevel;

    @Column(name = "duration_years")
    private Integer durationYears;

    @Column(name = "total_semesters")
    private Integer totalSemesters;

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(name = "is_active")
    private Boolean isActive;
}
