package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    private RoleName roleName;

    private String description;

    public Role(RoleName roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public enum RoleName {
        SUPER_ADMIN, ADMIN, FACULTY, STUDENT
    }
}

