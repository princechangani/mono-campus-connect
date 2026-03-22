package com.monocampusconnect.service.postgres;

import com.monocampusconnect.dto.postgres.DepartmentCanonicalRequest;
import com.monocampusconnect.dto.postgres.DepartmentCanonicalResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.DepartmentCanonical;
import com.monocampusconnect.repository.postgres.DepartmentCanonicalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentCanonicalService {

    private final DepartmentCanonicalRepository departmentRepository;

    public DepartmentCanonicalService(DepartmentCanonicalRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentCanonicalResponse create(DepartmentCanonicalRequest request) {
        String normalizedCode = request.getCode().trim().toUpperCase();
        if (departmentRepository.existsByTenantIdAndCodeAndIsDeletedFalse(request.getTenantId(), normalizedCode)) {
            throw new ApiException("Department code already exists: " + normalizedCode, 409);
        }

        DepartmentCanonical department = new DepartmentCanonical();
        department.setDepartmentPublicId(UUID.randomUUID());
        department.setTenantId(request.getTenantId());
        department.setName(request.getName().trim());
        department.setCode(normalizedCode);
        department.setHeadFacultyId(request.getHeadFacultyId());
        department.setParentDepartmentId(request.getParentDepartmentId());
        department.setDescription(request.getDescription());
        department.setCreatedAt(OffsetDateTime.now());

        return toResponse(departmentRepository.save(department));
    }

    public List<DepartmentCanonicalResponse> getAllByTenant(UUID tenantId) {
        return departmentRepository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DepartmentCanonicalResponse getByPublicId(UUID tenantId, UUID departmentPublicId) {
        return toResponse(getEntity(tenantId, departmentPublicId));
    }

    @Transactional
    public DepartmentCanonicalResponse update(UUID tenantId, UUID departmentPublicId, DepartmentCanonicalRequest request) {
        DepartmentCanonical department = getEntity(tenantId, departmentPublicId);

        String normalizedCode = request.getCode().trim().toUpperCase();
        if (!normalizedCode.equals(department.getCode())
                && departmentRepository.existsByTenantIdAndCodeAndIsDeletedFalse(tenantId, normalizedCode)) {
            throw new ApiException("Department code already exists: " + normalizedCode, 409);
        }

        department.setName(request.getName().trim());
        department.setCode(normalizedCode);
        department.setHeadFacultyId(request.getHeadFacultyId());
        department.setParentDepartmentId(request.getParentDepartmentId());
        department.setDescription(request.getDescription());
        department.setUpdatedAt(OffsetDateTime.now());

        return toResponse(departmentRepository.save(department));
    }

    @Transactional
    public void delete(UUID tenantId, UUID departmentPublicId) {
        DepartmentCanonical department = getEntity(tenantId, departmentPublicId);
        department.setIsDeleted(Boolean.TRUE);
        department.setDeletedAt(OffsetDateTime.now());
        departmentRepository.save(department);
    }

    private DepartmentCanonical getEntity(UUID tenantId, UUID departmentPublicId) {
        return departmentRepository.findByDepartmentPublicIdAndTenantIdAndIsDeletedFalse(departmentPublicId, tenantId)
                .orElseThrow(() -> new ApiException("Department not found", 404));
    }

    private DepartmentCanonicalResponse toResponse(DepartmentCanonical department) {
        DepartmentCanonicalResponse response = new DepartmentCanonicalResponse();
        response.setDepartmentId(department.getDepartmentId());
        response.setDepartmentPublicId(department.getDepartmentPublicId());
        response.setTenantId(department.getTenantId());
        response.setName(department.getName());
        response.setCode(department.getCode());
        response.setHeadFacultyId(department.getHeadFacultyId());
        response.setParentDepartmentId(department.getParentDepartmentId());
        response.setDescription(department.getDescription());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());
        return response;
    }
}
