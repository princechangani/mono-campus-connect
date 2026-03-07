package com.monocampusconnect.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private String message;
    private UserInfo user;

    @Data
    public static class UserInfo {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private String department;
        private String semester;
        private String phoneNumber;
        private String enrollmentNumber;
        private String facultyId;
        private java.util.UUID tenantId;

        public UserInfo(com.monocampusconnect.model.User u) {
            this.id               = u.getId();
            this.email            = u.getEmail();
            this.firstName        = u.getFirstName();
            this.lastName         = u.getLastName();
            this.role             = u.getRole().name();
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
                        com.monocampusconnect.model.User user) {
        this.token   = token;
        this.email   = email;
        this.role    = role;
        this.message = message;
        this.user    = new UserInfo(user);
    }

    // register response (no token)
    public AuthResponse(String email, String role, String message) {
        this.email   = email;
        this.role    = role;
        this.message = message;
    }

    // backward-compat constructor (no user object)
    public AuthResponse(String token, String email, String role, String message) {
        this.token   = token;
        this.email   = email;
        this.role    = role;
        this.message = message;
    }
}
