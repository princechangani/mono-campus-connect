package com.monocampusconnect.service;

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
            if (!roleRepository.existsByRoleName(name)) {
                roleRepository.save(new Role(name, name.name() + " role"));
            }
        }
    }

    // ─── Lookup helpers ───────────────────────────────────────────────────────

    public Role findByName(Role.RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ApiException("Role not found: " + roleName, 404));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // ─── Assign roles to a user ───────────────────────────────────────────────

    @Transactional
    public User assignRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found: " + userId, 404));

        for (String roleName : roleNames) {
            Role.RoleName rn;
            try {
                rn = Role.RoleName.valueOf(roleName.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException("Invalid role: " + roleName, 400);
            }
            Role role = findByName(rn);
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
        Set<Role.RoleName> parsed = roleNames.stream().map(r -> {
            try {
                return Role.RoleName.valueOf(r.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException("Invalid role: " + r, 400);
            }
        }).collect(Collectors.toSet());

        for (Role.RoleName rn : parsed) {
            Role role = findByName(rn);
            userRoleRepository.save(new UserRole(user, role));
        }

        // Keep the legacy single-role field in sync with the primary (first) role
        if (!parsed.isEmpty()) {
            Role.RoleName primary = parsed.iterator().next();
            user.setRole(User.Role.valueOf(primary.name()));
            userRepository.save(user);
        }

        return userRepository.findById(userId).orElseThrow();
    }

    // ─── Remove a single role from a user ────────────────────────────────────

    @Transactional
    public void removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found: " + userId, 404));
        Role.RoleName rn;
        try {
            rn = Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid role: " + roleName, 400);
        }
        Role role = findByName(rn);
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

    // ─── Get all users with a specific role (within tenant) ──────────────────

    public List<User> getUsersByRole(Role.RoleName roleName, java.util.UUID tenantId) {
        return userRoleRepository.findByRoleNameAndTenantId(roleName, tenantId)
                .stream()
                .map(UserRole::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    // ─── Assign initial role on user creation ────────────────────────────────

    @Transactional
    public void assignInitialRole(User user, Role.RoleName roleName) {
        Role role = findByName(roleName);
        if (!userRoleRepository.existsByUserAndRole(user, role)) {
            userRoleRepository.save(new UserRole(user, role));
        }
    }
}

