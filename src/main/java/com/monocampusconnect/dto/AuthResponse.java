package com.monocampusconnect.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private List<String> roles;
    private String message;
    private UserInfo user;

    @Data
    public static class UserInfo {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private List<String> roles;
        private String department;
        private String semester;
        private String phoneNumber;
        private String enrollmentNumber;
        private String facultyId;
        private java.util.UUID tenantId;

        public UserInfo(com.monocampusconnect.model.User u, String primaryRole, List<String> roleNames) {
            this.id               = u.getUserId();
            this.email            = u.getEmail();
            this.firstName        = u.getFirstName();
            this.lastName         = u.getLastName();
            this.role             = primaryRole;
            this.roles            = roleNames;
            this.department       = u.getDepartment();
            this.semester         = u.getSemester();
            this.phoneNumber      = u.getPhoneNumber();
            this.enrollmentNumber = u.getEnrollmentNumber();
            this.facultyId        = u.getFacultyId();
            this.tenantId         = u.getTenantId();
        }
    }

    // login response
    public AuthResponse(String token, String email, String role, String message,
                        com.monocampusconnect.model.User user, List<String> roles) {
        this.token   = token;
        this.email   = email;
        this.role    = role;
        this.roles   = roles;
        this.message = message;
        this.user    = new UserInfo(user, role, roles);
    }

    // register response (no token)
    public AuthResponse(String email, String role, String message) {
        this.email   = email;
        this.role    = role;
        this.roles   = Collections.singletonList(role);
        this.message = message;
    }

    // backward-compat constructor (no user object)
    public AuthResponse(String token, String email, String role, String message) {
        this.token   = token;
        this.email   = email;
        this.role    = role;
        this.roles   = Collections.singletonList(role);
        this.message = message;
    }
}
