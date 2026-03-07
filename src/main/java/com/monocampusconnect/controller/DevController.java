package com.monocampusconnect.controller;

import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DEV-ONLY endpoint — auto-disabled in production.
 * POST /api/dev/reset-passwords?password=YourPassword
 * Sets ALL users to the given plain-text password via Spring's PasswordEncoder.
 */
@RestController
@RequestMapping("/api/dev")
@Profile("!prod")
public class DevController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/reset-passwords")
    public ResponseEntity<Map<String, Object>> resetPasswords(
            @RequestParam(defaultValue = "Password@123") String password) {

        List<User> users = userRepository.findAll();
        String encoded = passwordEncoder.encode(password);
        users.forEach(u -> u.setPassword(encoded));
        userRepository.saveAll(users);

        return ResponseEntity.ok(Map.of(
            "message", "Reset " + users.size() + " user(s) password",
            "password", password,
            "hash",     encoded
        ));
    }

    @GetMapping("/hash")
    public ResponseEntity<Map<String, String>> getHash(
            @RequestParam String password) {
        return ResponseEntity.ok(Map.of(
            "password", password,
            "hash",     passwordEncoder.encode(password)
        ));
    }
}

