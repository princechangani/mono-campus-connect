package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "fee_structures")
public class FeeStructure extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_structure_id")
    private Long feeStructureId;

    @Column(name = "fee_structure_public_id")
    private UUID feeStructurePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "category")
    private String category;

    @Column(name = "fee_name")
    private String feeName;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "late_fee_per_day")
    private BigDecimal lateFeePerDay;
}

