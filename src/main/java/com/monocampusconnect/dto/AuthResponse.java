package com.monocampusconnect.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private String message;

    public AuthResponse(String token, String email, String role, String message) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    public AuthResponse(String email, String role, String message) {
        this.email = email;
        this.role = role;
        this.message = message;
    }
}
