package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.User;
import com.monocampusconnect.model.UserRole;
import com.monocampusconnect.repository.RoleRepository;
import com.monocampusconnect.repository.UserRepository;
import com.monocampusconnect.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserRoleRepository userRoleRepository;

    // ─── Seed default roles on startup ───────────────────────────────────────

    @PostConstruct
    @Transactional
    public void seedRoles() {
        for (Role.RoleName name : Role.RoleName.values()) {
            if (!roleRepository.existsByCode(name)) {
                Role role = new Role(name, name.name() + " role");
                role.setSystem(true);
                roleRepository.save(role);
            }
        }
    }

    // ─── Lookup helpers ───────────────────────────────────────────────────────

    public Role findByName(Role.RoleName roleName) {
        return roleRepository.findByCode(roleName)
                .orElseThrow(() -> new ApiException("Role not found: " + roleName, 404));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role.RoleName parseRoleName(String roleName) {
        try {
            return Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid role: " + roleName, 400);
        }
    }

    // ─── Assign roles to a user ───────────────────────────────────────────────

    @Transactional
    public User assignRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found: " + userId, 404));

        for (String roleName : roleNames) {
            Role role = findByName(parseRoleName(roleName));
            if (!userRoleRepository.existsByUserAndRole(user, role)) {
                userRoleRepository.save(new UserRole(user, role));
            }
        }
        return userRepository.findById(userId).orElseThrow();
    }

    // ─── Replace all roles for a user ────────────────────────────────────────

    @Transactional
    public User setRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found: " + userId, 404));

        // Remove all existing mappings
        userRoleRepository.deleteByUser(user);

        // Validate & convert
        Set<Role.RoleName> parsed = roleNames.stream()
                .map(this::parseRoleName)
                .collect(Collectors.toSet());

        for (Role.RoleName rn : parsed) {
            Role role = findByName(rn);
            userRoleRepository.save(new UserRole(user, role));
        }

        return userRepository.findById(userId).orElseThrow();
    }

    // ─── Remove a single role from a user ────────────────────────────────────

    @Transactional
    public void removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found: " + userId, 404));
        Role role = findByName(parseRoleName(roleName));
        UserRole mapping = userRoleRepository.findByUserAndRole(user, role)
                .orElseThrow(() -> new ApiException("User does not have role: " + roleName, 404));
        userRoleRepository.delete(mapping);
    }

    // ─── Get all roles of a user ──────────────────────────────────────────────

    public List<Role> getUserRoles(Long userId) {
        return userRoleRepository.findByUserId(userId)
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
    }

    public List<String> getUserRoleNames(Long userId) {
        return userRoleRepository.findByUserId(userId)
                .stream()
                .map(ur -> ur.getRole().getCode().name())
                .distinct()
                .collect(Collectors.toList());
    }

    // ─── Get all users with a specific role (within tenant) ──────────────────

    public List<User> getUsersByRole(Role.RoleName roleName, UUID tenantId) {
        return userRoleRepository.findUsersByRoleNameAndTenantId(roleName, tenantId);
    }

    public long countUsersByRole(Role.RoleName roleName, UUID tenantId) {
        return userRoleRepository.countUsersByRoleNameAndTenantId(roleName, tenantId);
    }

    // ─── Assign initial role on user creation ────────────────────────────────

    @Transactional
    public void assignInitialRole(User user, Role.RoleName roleName) {
        Role role = findByName(roleName);
        if (!userRoleRepository.existsByUserAndRole(user, role)) {
            userRoleRepository.save(new UserRole(user, role));
        }
    }

    // ─── Tenant-safe public ID role management ───────────────────────────────

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    private User findUserByPublicId(UUID userPublicId) {
        return userRepository.findByUsersPublicIdAndTenantId(userPublicId, currentTenant())
                .orElseThrow(() -> new ApiException("User not found", 404));
    }

    public List<Role> getUserRolesByPublicId(UUID userPublicId) {
        return getUserRoles(findUserByPublicId(userPublicId).getId());
    }

    @Transactional
    public User assignRolesByPublicId(UUID userPublicId, List<String> roleNames) {
        return assignRoles(findUserByPublicId(userPublicId).getId(), roleNames);
    }

    @Transactional
    public User setRolesByPublicId(UUID userPublicId, List<String> roleNames) {
        return setRoles(findUserByPublicId(userPublicId).getId(), roleNames);
    }

    @Transactional
    public void removeRoleByPublicId(UUID userPublicId, String roleName) {
        removeRole(findUserByPublicId(userPublicId).getId(), roleName);
    }
}
