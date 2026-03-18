package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.dto.canonical.postgres.TenantCanonicalRequest;
import com.monocampusconnect.dto.canonical.postgres.TenantCanonicalResponse;
import com.monocampusconnect.service.postgres.TenantCanonicalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/canonical/postgres/tenants")
public class TenantCanonicalController {

    private final TenantCanonicalService tenantService;

    public TenantCanonicalController(TenantCanonicalService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public ResponseEntity<TenantCanonicalResponse> create(@Valid @RequestBody TenantCanonicalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TenantCanonicalResponse>> getAll() {
        return ResponseEntity.ok(tenantService.getAll());
    }

    @GetMapping("/{tenantPublicId}")
    public ResponseEntity<TenantCanonicalResponse> getByPublicId(@PathVariable UUID tenantPublicId) {
        return ResponseEntity.ok(tenantService.getByPublicId(tenantPublicId));
    }

    @PutMapping("/{tenantPublicId}")
    public ResponseEntity<TenantCanonicalResponse> update(
            @PathVariable UUID tenantPublicId,
            @Valid @RequestBody TenantCanonicalRequest request
    ) {
        return ResponseEntity.ok(tenantService.update(tenantPublicId, request));
    }

    @DeleteMapping("/{tenantPublicId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID tenantPublicId) {
        tenantService.delete(tenantPublicId);
        return ResponseEntity.ok(Map.of("message", "Tenant deleted successfully"));
    }
}

