package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "faculty")
public class Faculty extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "faculty_public_id")
    private UUID facultyPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "designation")
    private String designation;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "status")
    private String status;
}

