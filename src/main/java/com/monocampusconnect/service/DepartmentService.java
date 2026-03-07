package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.DepartmentRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Department;
import com.monocampusconnect.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @Transactional
    public Department createDepartment(DepartmentRequest request) {
        UUID tenantId = currentTenant();
        if (departmentRepository.existsByTenantIdAndCode(tenantId, request.getCode().toUpperCase())) {
            throw new ApiException("Department code already exists: " + request.getCode(), 409);
        }
        Department dept = new Department();
        dept.setTenantId(tenantId);
        dept.setName(request.getName());
        dept.setCode(request.getCode().toUpperCase());
        dept.setHeadFacultyId(request.getHeadFacultyId());
        dept.setDescription(request.getDescription());
        return departmentRepository.save(dept);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findByTenantId(currentTenant());
    }

    public Department getDepartmentById(Long id) {
        UUID tenantId = currentTenant();
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Department not found", 404));
        if (!tenantId.equals(dept.getTenantId()))
            throw new ApiException("Department not found in this college", 404);
        return dept;
    }

    @Transactional
    public Department updateDepartment(Long id, DepartmentRequest request) {
        Department dept = getDepartmentById(id);
        dept.setName(request.getName());
        if (request.getHeadFacultyId() != null) dept.setHeadFacultyId(request.getHeadFacultyId());
        if (request.getDescription() != null) dept.setDescription(request.getDescription());
        return departmentRepository.save(dept);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.delete(getDepartmentById(id));
    }
}

