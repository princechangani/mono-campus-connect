package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "program_courses")
public class ProgramCourse extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_course_id")
    private Long programCourseId;

    @Column(name = "program_course_public_id")
    private UUID programCoursePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "is_elective")
    private Boolean isElective;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;
}

