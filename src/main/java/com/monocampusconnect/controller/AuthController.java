package com.monocampusconnect.controller;

import com.monocampusconnect.dto.AuthRequest;
import com.monocampusconnect.dto.AuthResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.User;
import com.monocampusconnect.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ApiException("Email is required", 400);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ApiException("Password is required", 400);
        }
        
        User user = authService.login(request);
        String token = authService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name(), "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ApiException("Email is required", 400);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ApiException("Password is required", 400);
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new ApiException("First name is required", 400);
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new ApiException("Last name is required", 400);
        }
        if (request.getRole() == null || request.getRole().trim().isEmpty()) {
            throw new ApiException("Role is required", 400);
        }
        
        User user = authService.register(request);
        return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getRole().name(), "Registration successful"));
    }
}
