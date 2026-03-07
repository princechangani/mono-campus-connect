package com.monocampusconnect.controller;

import com.monocampusconnect.dto.TenantRequest;
import com.monocampusconnect.dto.TenantResponse;
import com.monocampusconnect.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * SUPER_ADMIN only — manages college tenants on the platform.
 * All routes secured by SecurityConfig: hasRole("SUPER_ADMIN")
 */
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /** POST /api/tenants — onboard a new college */
    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(@Valid @RequestBody TenantRequest request) {
        TenantResponse response = tenantService.createTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** GET /api/tenants — list all colleges */
    @GetMapping
    public ResponseEntity<List<TenantResponse>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    /** GET /api/tenants/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    /** PUT /api/tenants/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable UUID id,
                                                       @Valid @RequestBody TenantRequest request) {
        return ResponseEntity.ok(tenantService.updateTenant(id, request));
    }

    /** PUT /api/tenants/{id}/enable */
    @PutMapping("/{id}/enable")
    public ResponseEntity<TenantResponse> enableTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.setTenantEnabled(id, true));
    }

    /** PUT /api/tenants/{id}/disable */
    @PutMapping("/{id}/disable")
    public ResponseEntity<TenantResponse> disableTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.setTenantEnabled(id, false));
    }
}

