package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "faculty_work_experience")
public class FacultyWorkExperience extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_experience_id")
    private Long workExperienceId;

    @Column(name = "work_experience_public_id")
    private UUID workExperiencePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "organization")
    private String organization;

    @Column(name = "role")
    private String role;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "description")
    private String description;
}

