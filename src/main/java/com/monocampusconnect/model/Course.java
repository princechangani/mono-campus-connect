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
    private String instructor;
    private String semester;


}
