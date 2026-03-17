package com.monocampusconnect.service;

import com.monocampusconnect.dto.ProfileRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.User;
import com.monocampusconnect.model.UserRole;
import com.monocampusconnect.repository.UserRepository;
import com.monocampusconnect.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Build authorities from user_role_mapping (multi-role support)
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities;
        if (!userRoles.isEmpty()) {
            authorities = userRoles.stream()
                    .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getRoleName().name()))
                    .collect(Collectors.toList());
        } else {
            // Fallback to legacy single role field
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));
    }

    public User updateProfile(Long userId, ProfileRequest request, MultipartFile profilePicture) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getSemester() != null) user.setSemester(request.getSemester());
        if (request.getEnrollmentNumber() != null) user.setEnrollmentNumber(request.getEnrollmentNumber());
        if (request.getFacultyId() != null) user.setFacultyId(request.getFacultyId());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        user.setUpdatedAt(new Date());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            user.setProfilePicture(profilePicture.getBytes());
        }
        return userRepository.save(user);
    }

    public void deleteProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));
        user.setProfilePicture(null);
        userRepository.save(user);
    }

    public User changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ApiException("Current password is incorrect", 400);
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new ApiException("New password must be at least 8 characters", 400);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }
}
