package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "academic_years")
public class AcademicYear extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "academic_year_public_id")
    private UUID academicYearPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "label")
    private String label;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private Boolean isCurrent;
}

