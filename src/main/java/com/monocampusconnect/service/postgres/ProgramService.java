package com.monocampusconnect.service.postgres;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Program;
import com.monocampusconnect.repository.postgres.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    /**
     * Create a new program
     */
    public Program create(Program program) {
        program.setTenantId(currentTenantId());
        if (program.getProgramPublicId() == null) {
            program.setProgramPublicId(UUID.randomUUID());
        }
        return programRepository.save(program);
    }

    /**
     * Get all programs for current tenant
     */
    public List<Program> getAll() {
        UUID tenantId = currentTenantId();
        return programRepository.findAll().stream()
                .filter(p -> tenantId.equals(p.getTenantId()))
                .toList();
    }

    /**
     * Get program by ID
     */
    public Program getById(Long id) {
        return programRepository.findById(id)
                .filter(p -> currentTenantId().equals(p.getTenantId()))
                .orElseThrow(() -> new ApiException("Program not found", 404));
    }

    /**
     * Get program by public ID
     */
    public Program getByPublicId(UUID publicId) {
        return programRepository.findAll().stream()
                .filter(p -> currentTenantId().equals(p.getTenantId()) && publicId.equals(p.getProgramPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Program not found", 404));
    }

    /**
     * Update a program
     */
    public Program update(Long id, Program program) {
        Program existing = getById(id);
        if (program.getName() != null)            existing.setName(program.getName());
        if (program.getCode() != null)            existing.setCode(program.getCode());
        if (program.getDepartmentId() != null)    existing.setDepartmentId(program.getDepartmentId());
        if (program.getDegreeLevel() != null)     existing.setDegreeLevel(program.getDegreeLevel());
        if (program.getDurationYears() != null)   existing.setDurationYears(program.getDurationYears());
        if (program.getTotalSemesters() != null)  existing.setTotalSemesters(program.getTotalSemesters());
        if (program.getTotalCredits() != null)    existing.setTotalCredits(program.getTotalCredits());
        if (program.getIsActive() != null)        existing.setIsActive(program.getIsActive());
        return programRepository.save(existing);
    }

    /**
     * Delete a program
     */
    public void delete(Long id) {
        Program program = getById(id);
        programRepository.delete(program);
    }

    /**
     * Get programs by department
     */
    public List<Program> getByDepartmentId(Long departmentId) {
        return programRepository.findAll().stream()
                .filter(p -> currentTenantId().equals(p.getTenantId()) && departmentId.equals(p.getDepartmentId()))
                .toList();
    }

    /**
     * Get program by code
     */
    public Program getByProgramCode(String programCode) {
        return programRepository.findAll().stream()
                .filter(p -> currentTenantId().equals(p.getTenantId()) && programCode.equals(p.getCode()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Program not found", 404));
    }
}

