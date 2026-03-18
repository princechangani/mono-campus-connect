package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "faculty_education")
public class FacultyEducation extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_education_id")
    private Long facultyEducationId;

    @Column(name = "faculty_education_public_id")
    private UUID facultyEducationPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "degree")
    private String degree;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "institution")
    private String institution;

    @Column(name = "passing_year")
    private Integer passingYear;

    @Column(name = "grade_or_percentage")
    private String gradeOrPercentage;
}

