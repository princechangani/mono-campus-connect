package com.monocampusconnect.service.mongo;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.AuditLogDocument;
import com.monocampusconnect.repository.mongo.AuditLogDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuditLogDocumentService {

    private final AuditLogDocumentRepository auditLogRepository;

    public AuditLogDocumentService(AuditLogDocumentRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Create a new audit log entry
     */
    public AuditLogDocument create(AuditLogDocument auditLog) {
        if (auditLog.getTenantId() == null || auditLog.getTenantId().isEmpty()) {
            throw new ApiException("Tenant ID is required", 400);
        }
        if (auditLog.getCreatedAt() == null) {
            auditLog.setCreatedAt(new Date());
        }
        return auditLogRepository.save(auditLog);
    }

    /**
     * Get all audit logs
     */
    public List<AuditLogDocument> getAll() {
        return auditLogRepository.findAll();
    }

    /**
     * Get audit log by ID
     */
    public AuditLogDocument getById(String id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new ApiException("Audit log not found", 404));
    }

    /**
     * Update an audit log (rarely needed)
     */
    public AuditLogDocument update(String id, AuditLogDocument auditLog) {
        AuditLogDocument existing = auditLogRepository.findById(id)
                .orElseThrow(() -> new ApiException("Audit log not found", 404));

        if (auditLog.getTenantId() != null) existing.setTenantId(auditLog.getTenantId());
        if (auditLog.getActorUserId() != null) existing.setActorUserId(auditLog.getActorUserId());
        if (auditLog.getActorRole() != null) existing.setActorRole(auditLog.getActorRole());
        if (auditLog.getAction() != null) existing.setAction(auditLog.getAction());
        if (auditLog.getResourceType() != null) existing.setResourceType(auditLog.getResourceType());
        if (auditLog.getResourceId() != null) existing.setResourceId(auditLog.getResourceId());

        return auditLogRepository.save(existing);
    }

    /**
     * Delete an audit log
     */
    public void delete(String id) {
        if (!auditLogRepository.existsById(id)) {
            throw new ApiException("Audit log not found", 404);
        }
        auditLogRepository.deleteById(id);
    }

    /**
     * Get audit logs by tenant
     */
    public List<AuditLogDocument> getByTenantId(String tenantId) {
        return auditLogRepository.findAll().stream()
                .filter(log -> tenantId.equals(log.getTenantId()))
                .toList();
    }

    /**
     * Get audit logs by resource type
     */
    public List<AuditLogDocument> getByResourceType(String resourceType) {
        return auditLogRepository.findAll().stream()
                .filter(log -> resourceType.equals(log.getResourceType()))
                .toList();
    }
}

