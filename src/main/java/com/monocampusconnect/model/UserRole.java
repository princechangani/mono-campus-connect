package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(
    name = "user_role_mapping",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
)
@Data
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "assigned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.assignedAt = new Date();
    }
}

