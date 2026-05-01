package com.monocampusconnect.service;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.dto.AuthRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.UserRepository;
import com.monocampusconnect.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        // [SECURITY FIX]: Force role to STUDENT. Prevents IDOR where an attacker sends "SUPER_ADMIN"
        Role.RoleName roleName = Role.RoleName.STUDENT;

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        User saved = userRepository.save(user);

        roleService.assignInitialRole(saved, roleName);

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

    public List<String> getRoleNames(User user) {
        return userRoleRepository.findByUserId(user.getUserId())
                .stream()
                .map(ur -> ur.getRole().getCode().name())
                .distinct()
                .collect(Collectors.toList());
    }

    public String getPrimaryRole(User user) {
        return getRoleNames(user).stream().findFirst().orElse("USER");
    }

    /** Generates a JWT with tenantId, userId, and all roles embedded */
    public String generateToken(User user) {
        Set<String> roleNames = getRoleNames(user).stream().collect(Collectors.toSet());
        return jwtConfig.generateToken(user, roleNames);
    }
}
