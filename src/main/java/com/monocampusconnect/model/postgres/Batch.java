package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "batches")
public class Batch extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_public_id")
    private UUID batchPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "name")
    private String name;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "class_teacher_id")
    private Long classTeacherId;
}

