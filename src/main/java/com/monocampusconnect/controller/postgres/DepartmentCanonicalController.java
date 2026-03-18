package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.dto.canonical.postgres.DepartmentCanonicalRequest;
import com.monocampusconnect.dto.canonical.postgres.DepartmentCanonicalResponse;
import com.monocampusconnect.service.postgres.DepartmentCanonicalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/canonical/postgres/tenants/{tenantId}/departments")
public class DepartmentCanonicalController {

    private final DepartmentCanonicalService departmentService;

    public DepartmentCanonicalController(DepartmentCanonicalService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentCanonicalResponse> create(
            @PathVariable UUID tenantId,
            @Valid @RequestBody DepartmentCanonicalRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentCanonicalResponse>> getAll(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(departmentService.getAllByTenant(tenantId));
    }

    @GetMapping("/{departmentPublicId}")
    public ResponseEntity<DepartmentCanonicalResponse> getByPublicId(
            @PathVariable UUID tenantId,
            @PathVariable UUID departmentPublicId
    ) {
        return ResponseEntity.ok(departmentService.getByPublicId(tenantId, departmentPublicId));
    }

    @PutMapping("/{departmentPublicId}")
    public ResponseEntity<DepartmentCanonicalResponse> update(
            @PathVariable UUID tenantId,
            @PathVariable UUID departmentPublicId,
            @Valid @RequestBody DepartmentCanonicalRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.ok(departmentService.update(tenantId, departmentPublicId, request));
    }

    @DeleteMapping("/{departmentPublicId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable UUID tenantId,
            @PathVariable UUID departmentPublicId
    ) {
        departmentService.delete(tenantId, departmentPublicId);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }
}
