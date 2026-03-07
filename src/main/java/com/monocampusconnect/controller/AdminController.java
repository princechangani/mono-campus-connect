package com.monocampusconnect.controller;

import com.monocampusconnect.dto.AdminDashboardStats;
import com.monocampusconnect.dto.AdminUserRequest;
import com.monocampusconnect.model.User;
import com.monocampusconnect.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ADMIN-only user & college management.
 * All routes secured: hasAnyRole("ADMIN","SUPER_ADMIN") in SecurityConfig.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /** GET /api/admin/stats — dashboard summary */
    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStats> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    /** GET /api/admin/users — all users in this tenant */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /** GET /api/admin/users/role/{role} — filter by role */
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }

    /** GET /api/admin/users/{id} */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    /** POST /api/admin/users — create a new faculty or student */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createUser(request));
    }

    /** PUT /api/admin/users/{id} — update user details */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }

    /** PUT /api/admin/users/{id}/enable */
    @PutMapping("/users/{id}/enable")
    public ResponseEntity<Map<String, String>> enableUser(@PathVariable Long id) {
        adminService.setUserEnabled(id, true);
        return ResponseEntity.ok(Map.of("message", "User enabled successfully"));
    }

    /** PUT /api/admin/users/{id}/disable */
    @PutMapping("/users/{id}/disable")
    public ResponseEntity<Map<String, String>> disableUser(@PathVariable Long id) {
        adminService.setUserEnabled(id, false);
        return ResponseEntity.ok(Map.of("message", "User disabled successfully"));
    }

    /** DELETE /api/admin/users/{id} */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}

