package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.RolePermission;
import com.monocampusconnect.repository.postgres.RolePermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RolePermission create(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    public List<RolePermission> getAll() {
        return rolePermissionRepository.findAll();
    }

    public RolePermission getById(Long id) {
        return rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ApiException("Role permission not found", 404));
    }

    public RolePermission update(Long id, RolePermission rolePermission) {
        RolePermission existing = getById(id);
        if (rolePermission.getRoleId() != null) existing.setRoleId(rolePermission.getRoleId());
        if (rolePermission.getPermissionId() != null) existing.setPermissionId(rolePermission.getPermissionId());
        return rolePermissionRepository.save(existing);
    }

    public void delete(Long id) {
        rolePermissionRepository.delete(getById(id));
    }

    public List<RolePermission> getByRoleId(Long roleId) {
        return rolePermissionRepository.findAll().stream()
                .filter(rp -> roleId.equals(rp.getRoleId()))
                .toList();
    }

    public List<RolePermission> getByPermissionId(Long permissionId) {
        return rolePermissionRepository.findAll().stream()
                .filter(rp -> permissionId.equals(rp.getPermissionId()))
                .toList();
    }
}

