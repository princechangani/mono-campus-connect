package com.monocampusconnect.controller;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.dto.ProfileRequest;
import com.monocampusconnect.model.User;
import com.monocampusconnect.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtConfig jwtConfig;

    private Long resolveUserId(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtConfig.extractUserId(header.substring(7));
        }
        throw new RuntimeException("Unable to resolve user from token");
    }

    /** GET /api/profile — get own profile */
    @GetMapping
    public ResponseEntity<User> getMyProfile(HttpServletRequest req) {
        return ResponseEntity.ok(profileService.getProfile(resolveUserId(req)));
    }

    /** GET /api/profile/{userId} — admin gets any profile */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    /** PUT /api/profile — update own profile (multipart) */
    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<User> updateProfile(
            HttpServletRequest req,
            @RequestPart(value = "firstName", required = false) String firstName,
            @RequestPart(value = "lastName", required = false) String lastName,
            @RequestPart(value = "phoneNumber", required = false) String phoneNumber,
            @RequestPart(value = "address", required = false) String address,
            @RequestPart(value = "department", required = false) String department,
            @RequestPart(value = "semester", required = false) String semester,
            @RequestPart(value = "enrollmentNumber", required = false) String enrollmentNumber,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) throws Exception {

        Long userId = resolveUserId(req);
        ProfileRequest request = new ProfileRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPhoneNumber(phoneNumber);
        request.setAddress(address);
        request.setDepartment(department);
        request.setSemester(semester);
        request.setEnrollmentNumber(enrollmentNumber);
        return ResponseEntity.ok(profileService.updateProfile(userId, request, profilePicture));
    }

    /** DELETE /api/profile/picture — remove profile picture */
    @DeleteMapping("/picture")
    public ResponseEntity<Map<String, String>> deleteProfilePicture(HttpServletRequest req) {
        profileService.deleteProfilePicture(resolveUserId(req));
        return ResponseEntity.ok(Map.of("message", "Profile picture removed"));
    }

    /** PUT /api/profile/password — change password */
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            HttpServletRequest req,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        profileService.changePassword(resolveUserId(req), currentPassword, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
