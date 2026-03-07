package com.monocampusconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @Lob
    private byte[] profilePicture;

    private String department;
    private String semester;
    private String enrollmentNumber;
    private String facultyId;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;

    public enum Role {
        SUPER_ADMIN, ADMIN, FACULTY, STUDENT
    }
}
