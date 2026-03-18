package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Permission;
import com.monocampusconnect.service.postgres.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<Permission> create(@RequestBody Permission permission) {
        return new ResponseEntity<>(permissionService.create(permission), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAll() {
        return ResponseEntity.ok(permissionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getById(id));
    }

    @GetMapping("/module/{module}/action/{action}")
    public ResponseEntity<Permission> getByModuleAndAction(@PathVariable String module, @PathVariable String action) {
        return ResponseEntity.ok(permissionService.getByModuleAndAction(module, action));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.update(id, permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

