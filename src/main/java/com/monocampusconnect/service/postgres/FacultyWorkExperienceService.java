package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.FacultyWorkExperience;
import com.monocampusconnect.repository.postgres.FacultyWorkExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyWorkExperienceService {

    private final FacultyWorkExperienceRepository facultyWorkExperienceRepository;

    public FacultyWorkExperienceService(FacultyWorkExperienceRepository facultyWorkExperienceRepository) {
        this.facultyWorkExperienceRepository = facultyWorkExperienceRepository;
    }

    public FacultyWorkExperience create(FacultyWorkExperience experience) {
        return facultyWorkExperienceRepository.save(experience);
    }

    public List<FacultyWorkExperience> getAll() {
        return facultyWorkExperienceRepository.findAll();
    }

    public FacultyWorkExperience getById(Long id) {
        return facultyWorkExperienceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Faculty work experience not found", 404));
    }

    public FacultyWorkExperience update(Long id, FacultyWorkExperience experience) {
        FacultyWorkExperience existing = getById(id);
        if (experience.getFacultyId() != null) existing.setFacultyId(experience.getFacultyId());
        if (experience.getOrganization() != null) existing.setOrganization(experience.getOrganization());
        if (experience.getRole() != null) existing.setRole(experience.getRole());
        if (experience.getFromDate() != null) existing.setFromDate(experience.getFromDate());
        if (experience.getToDate() != null) existing.setToDate(experience.getToDate());
        if (experience.getDescription() != null) existing.setDescription(experience.getDescription());
        return facultyWorkExperienceRepository.save(existing);
    }

    public void delete(Long id) {
        facultyWorkExperienceRepository.delete(getById(id));
    }

    public List<FacultyWorkExperience> getByFacultyId(Long facultyId) {
        return facultyWorkExperienceRepository.findAll().stream()
                .filter(fe -> facultyId.equals(fe.getFacultyId()))
                .toList();
    }
}

