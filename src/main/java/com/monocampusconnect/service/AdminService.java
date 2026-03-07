package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.AdminDashboardStats;
import com.monocampusconnect.dto.AdminUserRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ExamRepository examRepository;
    @Autowired private ResultRepository resultRepository;
    @Autowired private MaterialRepository materialRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    // ─── User Management ────────────────────────────────────────────────────

    @Transactional
    public User createUser(AdminUserRequest request) {
        UUID tenantId = currentTenant();

        if (userRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
            throw new ApiException("Email already in use in this college: " + request.getEmail(), 409);
        }

        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid role: " + request.getRole() + ". Must be FACULTY or STUDENT", 400);
        }

        if (role == User.Role.SUPER_ADMIN) {
            throw new ApiException("Cannot create SUPER_ADMIN via this endpoint", 403);
        }

        User user = new User();
        user.setTenantId(tenantId);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(role);
        user.setDepartment(request.getDepartment());
        user.setSemester(request.getSemester());
        user.setEnrollmentNumber(request.getEnrollmentNumber());
        user.setFacultyId(request.getFacultyId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEnabled(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findByTenantId(currentTenant());
    }

    public List<User> getUsersByRole(String roleName) {
        User.Role role;
        try {
            role = User.Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid role: " + roleName, 400);
        }
        return userRepository.findByTenantIdAndRole(currentTenant(), role);
    }

    public User getUserById(Long userId) {
        UUID tenantId = currentTenant();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found with id: " + userId, 404));
        if (!tenantId.equals(user.getTenantId())) {
            throw new ApiException("User not found in this college", 404);
        }
        return user;
    }

    @Transactional
    public User updateUser(Long userId, AdminUserRequest request) {
        User user = getUserById(userId);

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

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void setUserEnabled(Long userId, boolean enabled) {
        User user = getUserById(userId);
        user.setEnabled(enabled);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    // ─── Dashboard Stats ─────────────────────────────────────────────────────

    public AdminDashboardStats getDashboardStats() {
        UUID tenantId = currentTenant();
        AdminDashboardStats stats = new AdminDashboardStats();
        stats.setTotalStudents(userRepository.countByTenantIdAndRole(tenantId, User.Role.STUDENT));
        stats.setTotalFaculty(userRepository.countByTenantIdAndRole(tenantId, User.Role.FACULTY));
        stats.setTotalCourses(courseRepository.countByTenantId(tenantId));
        stats.setTotalExams(examRepository.countByTenantId(tenantId));
        stats.setTotalMaterials(materialRepository.countByTenantId(tenantId));
        stats.setTotalEvents(eventRepository.countByTenantId(tenantId));
        stats.setTotalDepartments(departmentRepository.countByTenantId(tenantId));
        stats.setTotalResults(resultRepository.countByTenantId(tenantId));
        return stats;
    }
}

