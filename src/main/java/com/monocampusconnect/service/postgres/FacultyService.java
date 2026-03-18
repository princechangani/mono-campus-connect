package com.monocampusconnect.service.postgres;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Faculty;
import com.monocampusconnect.repository.postgres.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    /**
     * Create a new faculty
     */
    public Faculty create(Faculty faculty) {
        faculty.setTenantId(currentTenantId());
        if (faculty.getFacultyPublicId() == null) {
            faculty.setFacultyPublicId(UUID.randomUUID());
        }
        return facultyRepository.save(faculty);
    }

    /**
     * Get all faculty for current tenant
     */
    public List<Faculty> getAll() {
        UUID tenantId = currentTenantId();
        return facultyRepository.findAll().stream()
                .filter(f -> tenantId.equals(f.getTenantId()))
                .toList();
    }

    /**
     * Get faculty by ID
     */
    public Faculty getById(Long id) {
        return facultyRepository.findById(id)
                .filter(f -> currentTenantId().equals(f.getTenantId()))
                .orElseThrow(() -> new ApiException("Faculty not found", 404));
    }

    /**
     * Get faculty by public ID
     */
    public Faculty getByPublicId(UUID publicId) {
        return facultyRepository.findAll().stream()
                .filter(f -> currentTenantId().equals(f.getTenantId()) && publicId.equals(f.getFacultyPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Faculty not found", 404));
    }

    /**
     * Update a faculty
     */
    public Faculty update(Long id, Faculty faculty) {
        Faculty existing = getById(id);
        if (faculty.getUserId() != null) existing.setUserId(faculty.getUserId());
        if (faculty.getDepartmentId() != null) existing.setDepartmentId(faculty.getDepartmentId());
        if (faculty.getDesignation() != null) existing.setDesignation(faculty.getDesignation());
        if (faculty.getEmployeeId() != null) existing.setEmployeeId(faculty.getEmployeeId());
        if (faculty.getEmploymentType() != null) existing.setEmploymentType(faculty.getEmploymentType());
        return facultyRepository.save(existing);
    }

    /**
     * Delete a faculty
     */
    public void delete(Long id) {
        Faculty faculty = getById(id);
        facultyRepository.delete(faculty);
    }

    /**
     * Get faculty by department
     */
    public List<Faculty> getByDepartmentId(Long departmentId) {
        return facultyRepository.findAll().stream()
                .filter(f -> currentTenantId().equals(f.getTenantId()) && departmentId.equals(f.getDepartmentId()))
                .toList();
    }

    /**
     * Get faculty by user ID
     */
    public Faculty getByUserId(Long userId) {
        return facultyRepository.findAll().stream()
                .filter(f -> currentTenantId().equals(f.getTenantId()) && userId.equals(f.getUserId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Faculty not found", 404));
    }
}

