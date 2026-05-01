package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.FeeStructure;
import com.monocampusconnect.repository.postgres.FeeStructureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeeStructureService {

    private final FeeStructureRepository feeStructureRepository;

    public FeeStructureService(FeeStructureRepository feeStructureRepository) {
        this.feeStructureRepository = feeStructureRepository;
    }

    public FeeStructure create(FeeStructure feeStructure) {
        return feeStructureRepository.save(feeStructure);
    }

    public List<FeeStructure> getAll() {
        return feeStructureRepository.findAll();
    }

    public FeeStructure getById(Long id) {
        return feeStructureRepository.findById(id)
                .orElseThrow(() -> new ApiException("Fee structure not found", 404));
    }

    public FeeStructure update(Long id, FeeStructure feeStructure) {
        FeeStructure existing = getById(id);
        if (feeStructure.getProgramId() != null) existing.setProgramId(feeStructure.getProgramId());
        if (feeStructure.getSemesterNumber() != null) existing.setSemesterNumber(feeStructure.getSemesterNumber());
        if (feeStructure.getAcademicYearId() != null) existing.setAcademicYearId(feeStructure.getAcademicYearId());
        if (feeStructure.getFeeName() != null) existing.setFeeName(feeStructure.getFeeName());
        if (feeStructure.getTotalAmount() != null) existing.setTotalAmount(feeStructure.getTotalAmount());
        return feeStructureRepository.save(existing);
    }

    public void delete(Long id) {
        feeStructureRepository.delete(getById(id));
    }

    public List<FeeStructure> getByProgramId(Long programId) {
        return feeStructureRepository.findAll().stream()
                .filter(fs -> programId.equals(fs.getProgramId()))
                .toList();
    }

    public List<FeeStructure> getByTenantId(UUID tenantId) {
        return feeStructureRepository.findAll().stream()
                .filter(fs -> tenantId.equals(fs.getTenantId()))
                .toList();
    }
}

