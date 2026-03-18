package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Permission;
import com.monocampusconnect.repository.postgres.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * Create a new permission
     */
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * Get all permissions
     */
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    /**
     * Get permission by ID
     */
    public Permission getById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ApiException("Permission not found", 404));
    }

    /**
     * Get permission by name
     */
    public Permission getByModuleAndAction(String module, String action) {
        return permissionRepository.findAll().stream()
                .filter(p -> module.equals(p.getModule()) && action.equals(p.getAction()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Permission not found", 404));
    }

    /**
     * Update a permission
     */
    public Permission update(Long id, Permission permission) {
        Permission existing = getById(id);
        if (permission.getModule() != null) existing.setModule(permission.getModule());
        if (permission.getAction() != null) existing.setAction(permission.getAction());
        if (permission.getResource() != null) existing.setResource(permission.getResource());
        return permissionRepository.save(existing);
    }

    /**
     * Delete a permission
     */
    public void delete(Long id) {
        Permission permission = getById(id);
        permissionRepository.delete(permission);
    }
}

