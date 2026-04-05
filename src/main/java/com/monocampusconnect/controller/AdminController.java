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
import java.util.UUID;

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

    /** GET /api/admin/students — alias for STUDENT role */
    @GetMapping("/students")
    public ResponseEntity<List<User>> getStudents() {
        return ResponseEntity.ok(adminService.getUsersByRole("STUDENT"));
    }

    /** GET /api/admin/users/{publicId} */
    @GetMapping("/users/{publicId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID publicId) {
        return ResponseEntity.ok(adminService.getUserByPublicId(publicId));
    }

    /** GET /api/admin/users/id/{id} — numeric id lookup (frontend compatibility) */
    @GetMapping("/users/id/{id}")
    public ResponseEntity<User> getUserByNumericId(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAllUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new com.monocampusconnect.exception.ApiException("User not found", 404)));
    }

    /** POST /api/admin/users — create a new faculty or student */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createUser(request));
    }

    /** PUT /api/admin/users/{publicId} — update user details */
    @PutMapping("/users/{publicId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID publicId,
                                           @Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.ok(adminService.updateUserByPublicId(publicId, request));
    }

    /** PUT /api/admin/users/id/{id} — numeric id update (frontend compatibility) */
    @PutMapping("/users/id/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id,
                                               @Valid @RequestBody AdminUserRequest request) {
        User user = adminService.getAllUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new com.monocampusconnect.exception.ApiException("User not found", 404));
        return ResponseEntity.ok(adminService.updateUserByPublicId(user.getUsersPublicId(), request));
    }

    /** PUT /api/admin/users/{publicId}/enable */
    @PutMapping("/users/{publicId}/enable")
    public ResponseEntity<Map<String, String>> enableUser(@PathVariable UUID publicId) {
        adminService.setUserEnabledByPublicId(publicId, true);
        return ResponseEntity.ok(Map.of("message", "User enabled successfully"));
    }

    /** PUT /api/admin/users/id/{id}/enable — numeric id enable (frontend compatibility) */
    @PutMapping("/users/id/{id}/enable")
    public ResponseEntity<Map<String, String>> enableUserById(@PathVariable Long id) {
        User user = adminService.getAllUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new com.monocampusconnect.exception.ApiException("User not found", 404));
        adminService.setUserEnabledByPublicId(user.getUsersPublicId(), true);
        return ResponseEntity.ok(Map.of("message", "User enabled successfully"));
    }

    /** PUT /api/admin/users/{publicId}/disable */
    @PutMapping("/users/{publicId}/disable")
    public ResponseEntity<Map<String, String>> disableUser(@PathVariable UUID publicId) {
        adminService.setUserEnabledByPublicId(publicId, false);
        return ResponseEntity.ok(Map.of("message", "User disabled successfully"));
    }

    /** PUT /api/admin/users/id/{id}/disable — numeric id disable (frontend compatibility) */
    @PutMapping("/users/id/{id}/disable")
    public ResponseEntity<Map<String, String>> disableUserById(@PathVariable Long id) {
        User user = adminService.getAllUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new com.monocampusconnect.exception.ApiException("User not found", 404));
        adminService.setUserEnabledByPublicId(user.getUsersPublicId(), false);
        return ResponseEntity.ok(Map.of("message", "User disabled successfully"));
    }

    /** DELETE /api/admin/users/{publicId} */
    @DeleteMapping("/users/{publicId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID publicId) {
        adminService.deleteUserByPublicId(publicId);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    /** DELETE /api/admin/users/id/{id} — numeric id delete (frontend compatibility) */
    @DeleteMapping("/users/id/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable Long id) {
        User user = adminService.getAllUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new com.monocampusconnect.exception.ApiException("User not found", 404));
        adminService.deleteUserByPublicId(user.getUsersPublicId());
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
