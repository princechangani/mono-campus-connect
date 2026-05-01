package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.DepartmentCanonical;
import com.monocampusconnect.repository.postgres.DepartmentCanonicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentCanonicalRepository departmentRepository;

    public DepartmentController(DepartmentCanonicalRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        return ResponseEntity.ok(
                departmentRepository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(currentTenantId())
                        .stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody DepartmentCanonical payload) {
        DepartmentCanonical dept = new DepartmentCanonical();
        dept.setTenantId(currentTenantId());
        dept.setName(payload.getName());
        dept.setCode(payload.getCode());
        dept.setDescription(payload.getDescription());
        dept.setHeadFacultyId(payload.getHeadFacultyId());
        dept.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(departmentRepository.save(dept)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DepartmentCanonical payload) {
        UUID tenantId = currentTenantId();
        DepartmentCanonical dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Department not found", 404));
        if (!tenantId.equals(dept.getTenantId())) throw new ApiException("Forbidden", 403);

        if (payload.getName() != null) dept.setName(payload.getName());
        if (payload.getCode() != null) dept.setCode(payload.getCode());
        if (payload.getDescription() != null) dept.setDescription(payload.getDescription());
        if (payload.getHeadFacultyId() != null) dept.setHeadFacultyId(payload.getHeadFacultyId());
        dept.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(toDto(departmentRepository.save(dept)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        UUID tenantId = currentTenantId();
        DepartmentCanonical dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Department not found", 404));
        if (!tenantId.equals(dept.getTenantId())) throw new ApiException("Forbidden", 403);
        dept.setIsDeleted(Boolean.TRUE);
        dept.setDeletedAt(OffsetDateTime.now());
        departmentRepository.save(dept);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toDto(DepartmentCanonical d) {
        Map<String, Object> row = new java.util.LinkedHashMap<>();
        row.put("id", d.getDepartmentId());
        row.put("name", d.getName());
        row.put("code", d.getCode());
        row.put("description", d.getDescription());
        row.put("headFacultyId", d.getHeadFacultyId());
        return row;
    }
}
