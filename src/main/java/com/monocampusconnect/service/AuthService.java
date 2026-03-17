package com.monocampusconnect.service;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.dto.AuthRequest;
import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.User;
import com.monocampusconnect.model.UserRole;
import com.monocampusconnect.repository.UserRepository;
import com.monocampusconnect.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final RoleService roleService;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtConfig jwtConfig, RoleService roleService,
                       UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.roleService = roleService;
        this.userRoleRepository = userRoleRepository;
    }

    public User register(AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiException("Email already in use: " + request.getEmail(), 409);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        User.Role role = User.Role.valueOf(request.getRole().toUpperCase());
        user.setRole(role);
        user.setEnabled(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        User saved = userRepository.save(user);

        // Register role in user_role_mapping for multi-role support
        roleService.assignInitialRole(saved, Role.RoleName.valueOf(role.name()));

        return saved;
    }

    public User login(AuthRequest request) {
        if (request == null) {
            throw new ApiException("Request body is required", 400);
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("No account found with email: " + request.getEmail(), 404));

        if (!user.isEnabled()) {
            throw new ApiException("Account is disabled. Please contact admin.", 403);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials", 401);
        }
        return user;
    }

    /** Generates a JWT with tenantId, userId, and all roles embedded */
    public String generateToken(User user) {
        Set<String> roleNames = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(ur -> ur.getRole().getRoleName().name())
                .collect(Collectors.toSet());
        return jwtConfig.generateToken(user, roleNames);
    }
}
