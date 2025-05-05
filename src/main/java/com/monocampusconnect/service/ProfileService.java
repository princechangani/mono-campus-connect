package com.monocampusconnect.service;

import com.monocampusconnect.dto.ProfileRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import com.monocampusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class ProfileService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRole().equals(User.Role.ADMIN)
                        ? Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        : Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));
    }

    public User updateProfile(Long userId, ProfileRequest request, MultipartFile profilePicture) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));

        // Update basic information
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDepartment(request.getDepartment());
        user.setSemester(request.getSemester());
        user.setEnrollmentNumber(request.getEnrollmentNumber());
        user.setFacultyId(request.getFacultyId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setUpdatedAt(new Date());

        // Handle profile picture
        if (profilePicture != null && !profilePicture.isEmpty()) {
            // Store new profile picture directly in the database
            user.setProfilePicture(profilePicture.getBytes());
        }

        return userRepository.save(user);
    }

    public void deleteProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));

        // Set profile picture to null
        user.setProfilePicture(null);
        userRepository.save(user);
    }

    public User changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));

        // TODO: Add password validation and update logic
        return userRepository.save(user);
    }
}
