package com.monocampusconnect.controller;

import com.monocampusconnect.dto.RoleAssignRequest;
import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.User;
import com.monocampusconnect.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * GET /api/roles
     * List all available roles in the system.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /**
     * GET /api/roles/user/{userPublicId}
     * Get all roles assigned to a specific user.
     */
    @GetMapping("/user/{userPublicId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable UUID userPublicId) {
        return ResponseEntity.ok(roleService.getUserRolesByPublicId(userPublicId));
    }

    /**
     * POST /api/roles/user/{userPublicId}/assign
     * Assign one or more roles to a user (additive — does not remove existing roles).
     * Body: { "userId": 1, "roles": ["ADMIN", "FACULTY"] }
     */
    @PostMapping("/user/{userPublicId}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<User> assignRoles(@PathVariable UUID userPublicId,
                                            @RequestBody RoleAssignRequest request) {
        User updated = roleService.assignRolesByPublicId(userPublicId, request.getRoles());
        return ResponseEntity.ok(updated);
    }

    /**
     * PUT /api/roles/user/{userPublicId}/set
     * Replace ALL roles for a user with the provided list.
     * Body: { "userId": 1, "roles": ["FACULTY", "STUDENT"] }
     */
    @PutMapping("/user/{userPublicId}/set")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<User> setRoles(@PathVariable UUID userPublicId,
                                         @RequestBody RoleAssignRequest request) {
        User updated = roleService.setRolesByPublicId(userPublicId, request.getRoles());
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/roles/user/{userPublicId}/remove/{roleName}
     * Remove a single role from a user.
     */
    @DeleteMapping("/user/{userPublicId}/remove/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> removeRole(
            @PathVariable UUID userPublicId,
            @PathVariable String roleName) {
        roleService.removeRoleByPublicId(userPublicId, roleName);
        return ResponseEntity.ok(Map.of("message", "Role " + roleName + " removed from user " + userPublicId));
    }
}
