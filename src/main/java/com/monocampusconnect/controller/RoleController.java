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
     * GET /api/roles/user/{userId}
     * Get all roles assigned to a specific user.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable Long userId) {
        return ResponseEntity.ok(roleService.getUserRoles(userId));
    }

    /**
     * POST /api/roles/assign
     * Assign one or more roles to a user (additive — does not remove existing roles).
     * Body: { "userId": 1, "roles": ["ADMIN", "FACULTY"] }
     */
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<User> assignRoles(@RequestBody RoleAssignRequest request) {
        User updated = roleService.assignRoles(request.getUserId(), request.getRoles());
        return ResponseEntity.ok(updated);
    }

    /**
     * PUT /api/roles/set
     * Replace ALL roles for a user with the provided list.
     * Body: { "userId": 1, "roles": ["FACULTY", "STUDENT"] }
     */
    @PutMapping("/set")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<User> setRoles(@RequestBody RoleAssignRequest request) {
        User updated = roleService.setRoles(request.getUserId(), request.getRoles());
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/roles/user/{userId}/remove/{roleName}
     * Remove a single role from a user.
     */
    @DeleteMapping("/user/{userId}/remove/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> removeRole(
            @PathVariable Long userId,
            @PathVariable String roleName) {
        roleService.removeRole(userId, roleName);
        return ResponseEntity.ok(Map.of("message", "Role " + roleName + " removed from user " + userId));
    }
}

