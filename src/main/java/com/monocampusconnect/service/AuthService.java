package com.monocampusconnect.service;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.dto.AuthRequest;
import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
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
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setEnabled(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
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

    /** Generates a JWT with tenantId, userId, and role embedded */
    public String generateToken(User user) {
        return jwtConfig.generateToken(user);
    }
}
