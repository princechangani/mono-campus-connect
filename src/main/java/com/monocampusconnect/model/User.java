package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String department;
    private String semester;
    private String enrollmentNumber;
    private String facultyId;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private Role role;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;



    public enum Role {
        ADMIN, FACULTY, STUDENT
    }
}
