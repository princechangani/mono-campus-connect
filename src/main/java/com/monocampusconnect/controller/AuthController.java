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
    public ResponseEntity<AuthResponse> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        
        if (email == null || email.trim().isEmpty()) {
            throw new ApiException("Email is required", 400);
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ApiException("Password is required", 400);
        }
        
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);
        
        User user = authService.login(request);
        String token = authService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name(), "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") String role) {
        
        if (email == null || email.trim().isEmpty()) {
            throw new ApiException("Email is required", 400);
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ApiException("Password is required", 400);
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ApiException("First name is required", 400);
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ApiException("Last name is required", 400);
        }
        if (role == null || role.trim().isEmpty()) {
            throw new ApiException("Role is required", 400);
        }
        
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setRole(role);
        
        User user = authService.register(request);
        return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getRole().name(), "Registration successful"));
    }
}
