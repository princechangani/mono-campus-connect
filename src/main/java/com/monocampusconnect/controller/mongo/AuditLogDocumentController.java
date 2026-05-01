package com.monocampusconnect.controller.mongo;

import com.monocampusconnect.model.mongo.AuditLogDocument;
import com.monocampusconnect.service.mongo.AuditLogDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents/audit-logs")
public class AuditLogDocumentController {

    private final AuditLogDocumentService auditLogDocumentService;

    public AuditLogDocumentController(AuditLogDocumentService auditLogDocumentService) {
        this.auditLogDocumentService = auditLogDocumentService;
    }

    @PostMapping
    public ResponseEntity<AuditLogDocument> create(@RequestBody AuditLogDocument auditLog) {
        return new ResponseEntity<>(auditLogDocumentService.create(auditLog), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDocument>> getAll() {
        return ResponseEntity.ok(auditLogDocumentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDocument> getById(@PathVariable String id) {
        return ResponseEntity.ok(auditLogDocumentService.getById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<AuditLogDocument>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(auditLogDocumentService.getByTenantId(tenantId));
    }

    @GetMapping("/resource-type/{resourceType}")
    public ResponseEntity<List<AuditLogDocument>> getByResourceType(@PathVariable String resourceType) {
        return ResponseEntity.ok(auditLogDocumentService.getByResourceType(resourceType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogDocument> update(@PathVariable String id, @RequestBody AuditLogDocument auditLog) {
        return ResponseEntity.ok(auditLogDocumentService.update(id, auditLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        auditLogDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

