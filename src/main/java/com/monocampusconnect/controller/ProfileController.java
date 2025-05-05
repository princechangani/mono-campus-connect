package com.monocampusconnect.controller;

import com.monocampusconnect.dto.ProfileRequest;
import com.monocampusconnect.model.User;
import com.monocampusconnect.service.ProfileService;
import com.monocampusconnect.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<User> getProfile(@RequestPart("userId") Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestPart(value = "firstName", required = false) String firstName,
            @RequestPart(value = "lastName", required = false) String lastName,
            @RequestPart(value = "email", required = false) String email,
            @RequestPart(value = "phone", required = false) String phone,
            @RequestPart(value = "address", required = false) String address,
            @RequestPart(required = false) MultipartFile profilePicture) {
        
        try {
            ProfileRequest request = new ProfileRequest();
            request.setFirstName(firstName);
            request.setLastName(lastName);
            request.setEmail(email);
            request.setPhoneNumber(phone);
            request.setAddress(address);
            
            return ResponseEntity.ok(profileService.updateProfile(userId, request, profilePicture));
        } catch (ApiException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating profile: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/picture")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long userId) {
        profileService.deleteProfilePicture(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<Void> changePassword(@PathVariable Long userId,
                                              @RequestParam String currentPassword,
                                              @RequestParam String newPassword) {
        profileService.changePassword(userId, currentPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
