package com.monocampusconnect.service.postgres;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Batch;
import com.monocampusconnect.repository.postgres.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    /**
     * Create a new batch
     */
    public Batch create(Batch batch) {
        batch.setTenantId(currentTenantId());
        if (batch.getBatchPublicId() == null) {
            batch.setBatchPublicId(UUID.randomUUID());
        }
        return batchRepository.save(batch);
    }

    /**
     * Get all batches for current tenant
     */
    public List<Batch> getAll() {
        UUID tenantId = currentTenantId();
        return batchRepository.findAll().stream()
                .filter(b -> tenantId.equals(b.getTenantId()))
                .toList();
    }

    /**
     * Get batch by ID
     */
    public Batch getById(Long id) {
        return batchRepository.findById(id)
                .filter(b -> currentTenantId().equals(b.getTenantId()))
                .orElseThrow(() -> new ApiException("Batch not found", 404));
    }

    /**
     * Get batch by public ID
     */
    public Batch getByPublicId(UUID publicId) {
        return batchRepository.findAll().stream()
                .filter(b -> currentTenantId().equals(b.getTenantId()) && publicId.equals(b.getBatchPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Batch not found", 404));
    }

    /**
     * Update a batch
     */
    public Batch update(Long id, Batch batch) {
        Batch existing = getById(id);
        if (batch.getName() != null)             existing.setName(batch.getName());
        if (batch.getProgramId() != null)        existing.setProgramId(batch.getProgramId());
        if (batch.getAcademicYearId() != null)   existing.setAcademicYearId(batch.getAcademicYearId());
        if (batch.getCurrentSemester() != null)  existing.setCurrentSemester(batch.getCurrentSemester());
        if (batch.getMaxStudents() != null)      existing.setMaxStudents(batch.getMaxStudents());
        if (batch.getClassTeacherId() != null)   existing.setClassTeacherId(batch.getClassTeacherId());
        return batchRepository.save(existing);
    }
    /**
     * Delete a batch
     */
    public void delete(Long id) {
        Batch batch = getById(id);
        batchRepository.delete(batch);
    }

    /**
     * Get batches by program
     */
    public List<Batch> getByProgramId(Long programId) {
        return batchRepository.findAll().stream()
                .filter(b -> currentTenantId().equals(b.getTenantId()) && programId.equals(b.getProgramId()))
                .toList();
    }

    /**
     * Get batch by code
     */
    public Batch getByBatchCode(String batchCode) {
        return batchRepository.findAll().stream()
                .filter(b -> {
                    currentTenantId();
                    return false;
                })
                .findFirst()
                .orElseThrow(() -> new ApiException("Batch not found", 404));
    }
}

