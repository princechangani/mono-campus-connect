package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.RolePermission;
import com.monocampusconnect.service.postgres.RolePermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @PostMapping
    public ResponseEntity<RolePermission> create(@RequestBody RolePermission rolePermission) {
        return new ResponseEntity<>(rolePermissionService.create(rolePermission), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RolePermission>> getAll() {
        return ResponseEntity.ok(rolePermissionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolePermission> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rolePermissionService.getById(id));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermission>> getByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(rolePermissionService.getByRoleId(roleId));
    }

    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<RolePermission>> getByPermissionId(@PathVariable Long permissionId) {
        return ResponseEntity.ok(rolePermissionService.getByPermissionId(permissionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolePermission> update(@PathVariable Long id, @RequestBody RolePermission rolePermission) {
        return ResponseEntity.ok(rolePermissionService.update(id, rolePermission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolePermissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

