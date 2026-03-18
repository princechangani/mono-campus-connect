package com.monocampusconnect.service.postgres;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.AcademicYear;
import com.monocampusconnect.repository.postgres.AcademicYearRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    public AcademicYearService(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public AcademicYear create(AcademicYear academicYear) {
        academicYear.setTenantId(currentTenantId());
        if (academicYear.getAcademicYearPublicId() == null) {
            academicYear.setAcademicYearPublicId(UUID.randomUUID());
        }
        return academicYearRepository.save(academicYear);
    }

    public List<AcademicYear> getAll() {
        UUID tenantId = currentTenantId();
        return academicYearRepository.findAll().stream()
                .filter(y -> tenantId.equals(y.getTenantId()))
                .toList();
    }

    public AcademicYear getById(Long id) {
        return academicYearRepository.findById(id)
                .filter(y -> currentTenantId().equals(y.getTenantId()))
                .orElseThrow(() -> new ApiException("Academic year not found", 404));
    }

    public AcademicYear getByPublicId(UUID publicId) {
        return academicYearRepository.findAll().stream()
                .filter(y -> currentTenantId().equals(y.getTenantId()) && publicId.equals(y.getAcademicYearPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Academic year not found", 404));
    }

    public AcademicYear update(Long id, AcademicYear academicYear) {
        AcademicYear existing = getById(id);
        if (academicYear.getLabel() != null) existing.setLabel(academicYear.getLabel());
        if (academicYear.getStartDate() != null) existing.setStartDate(academicYear.getStartDate());
        if (academicYear.getEndDate() != null) existing.setEndDate(academicYear.getEndDate());
        if (academicYear.getIsCurrent() != null) existing.setIsCurrent(academicYear.getIsCurrent());
        return academicYearRepository.save(existing);
    }

    public void delete(Long id) {
        AcademicYear academicYear = getById(id);
        academicYearRepository.delete(academicYear);
    }

    public AcademicYear getCurrentAcademicYear() {
        return academicYearRepository.findAll().stream()
                .filter(y -> currentTenantId().equals(y.getTenantId()) && Boolean.TRUE.equals(y.getIsCurrent()))
                .findFirst()
                .orElseThrow(() -> new ApiException("No active academic year found", 404));
    }

    public AcademicYear getByLabel(String label) {
        return academicYearRepository.findAll().stream()
                .filter(y -> currentTenantId().equals(y.getTenantId()) && label.equals(y.getLabel()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Academic year not found", 404));
    }
}

