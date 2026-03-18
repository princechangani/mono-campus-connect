package com.monocampusconnect.model.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "students")
public class Student extends BaseAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_public_id")
    private UUID studentPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "enrollment_number")
    private String enrollmentNumber;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name = "admission_type")
    private String admissionType;

    @Column(name = "status")
    private String status;
}
