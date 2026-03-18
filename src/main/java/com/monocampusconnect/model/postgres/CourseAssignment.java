package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "course_assignments")
public class CourseAssignment extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_assignment_id")
    private Long courseAssignmentId;

    @Column(name = "course_assignment_public_id")
    private UUID courseAssignmentPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "section")
    private String section;

    @Column(name = "is_primary")
    private Boolean isPrimary;
}

