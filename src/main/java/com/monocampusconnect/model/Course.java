package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String courseCode;
    private String courseName;
    private String department;
    private int credits;
    private String instructor;   // faculty name
    private String facultyId;    // faculty user id / FAC001 etc.
    private String semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type")
    private SubjectType subjectType = SubjectType.COMPULSORY;

    private String category;     // e.g. "Core", "Lab", "Elective", "Project"

    public enum SubjectType {
        COMPULSORY, OPTIONAL
    }
}
