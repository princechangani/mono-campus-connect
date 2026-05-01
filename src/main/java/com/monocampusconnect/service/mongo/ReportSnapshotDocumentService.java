package com.monocampusconnect.service.mongo;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.ReportSnapshotDocument;
import com.monocampusconnect.repository.mongo.ReportSnapshotDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReportSnapshotDocumentService {

    private final ReportSnapshotDocumentRepository reportSnapshotRepository;

    public ReportSnapshotDocumentService(ReportSnapshotDocumentRepository reportSnapshotRepository) {
        this.reportSnapshotRepository = reportSnapshotRepository;
    }

    /**
     * Create a new report snapshot
     */
    public ReportSnapshotDocument create(ReportSnapshotDocument snapshot) {
        if (snapshot.getTenantId() == null || snapshot.getTenantId().isEmpty()) {
            throw new ApiException("Tenant ID is required", 400);
        }
        if (snapshot.getPublicId() == null || snapshot.getPublicId().isEmpty()) {
            snapshot.setPublicId(UUID.randomUUID().toString());
        }
        if (snapshot.getGeneratedAt() == null) {
            snapshot.setGeneratedAt(new Date());
        }
        return reportSnapshotRepository.save(snapshot);
    }

    /**
     * Get all report snapshots
     */
    public List<ReportSnapshotDocument> getAll() {
        return reportSnapshotRepository.findAll();
    }

    /**
     * Get report snapshot by ID
     */
    public ReportSnapshotDocument getById(String id) {
        return reportSnapshotRepository.findById(id)
                .orElseThrow(() -> new ApiException("Report snapshot not found", 404));
    }

    /**
     * Get report snapshot by public ID
     */
    public ReportSnapshotDocument getByPublicId(String tenantId, String publicId) {
        return reportSnapshotRepository.findAll().stream()
                .filter(snapshot -> tenantId.equals(snapshot.getTenantId()) && publicId.equals(snapshot.getPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Report snapshot not found", 404));
    }

    /**
     * Update a report snapshot
     */
    public ReportSnapshotDocument update(String id, ReportSnapshotDocument snapshot) {
        ReportSnapshotDocument existing = reportSnapshotRepository.findById(id)
                .orElseThrow(() -> new ApiException("Report snapshot not found", 404));

        if (snapshot.getReportType() != null) existing.setReportType(snapshot.getReportType());
        if (snapshot.getReferenceType() != null) existing.setReferenceType(snapshot.getReferenceType());
        if (snapshot.getReferenceId() != null) existing.setReferenceId(snapshot.getReferenceId());
        if (snapshot.getData() != null) existing.setData(snapshot.getData());
        if (snapshot.getExpiresAt() != null) existing.setExpiresAt(snapshot.getExpiresAt());

        return reportSnapshotRepository.save(existing);
    }

    /**
     * Delete a report snapshot
     */
    public void delete(String id) {
        if (!reportSnapshotRepository.existsById(id)) {
            throw new ApiException("Report snapshot not found", 404);
        }
        reportSnapshotRepository.deleteById(id);
    }

    /**
     * Get snapshots by tenant
     */
    public List<ReportSnapshotDocument> getByTenantId(String tenantId) {
        return reportSnapshotRepository.findAll().stream()
                .filter(snapshot -> tenantId.equals(snapshot.getTenantId()))
                .toList();
    }

    /**
     * Get snapshots by report type
     */
    public List<ReportSnapshotDocument> getByReportType(String reportType) {
        return reportSnapshotRepository.findAll().stream()
                .filter(snapshot -> reportType.equals(snapshot.getReportType()))
                .toList();
    }

    /**
     * Get snapshots by user
     */
    public List<ReportSnapshotDocument> getByGeneratedBy(Long userId) {
        return reportSnapshotRepository.findAll().stream()
                .filter(snapshot -> userId.equals(snapshot.getGeneratedBy()))
                .toList();
    }
}

