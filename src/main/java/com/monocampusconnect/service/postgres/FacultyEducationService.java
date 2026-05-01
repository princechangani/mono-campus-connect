package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.FacultyEducation;
import com.monocampusconnect.repository.postgres.FacultyEducationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyEducationService {

    private final FacultyEducationRepository facultyEducationRepository;

    public FacultyEducationService(FacultyEducationRepository facultyEducationRepository) {
        this.facultyEducationRepository = facultyEducationRepository;
    }

    public FacultyEducation create(FacultyEducation education) {
        return facultyEducationRepository.save(education);
    }

    public List<FacultyEducation> getAll() {
        return facultyEducationRepository.findAll();
    }

    public FacultyEducation getById(Long id) {
        return facultyEducationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Faculty education not found", 404));
    }

    public FacultyEducation update(Long id, FacultyEducation education) {
        FacultyEducation existing = getById(id);
        if (education.getFacultyId() != null)          existing.setFacultyId(education.getFacultyId());
        if (education.getDegree() != null)             existing.setDegree(education.getDegree());
        if (education.getFieldOfStudy() != null)       existing.setFieldOfStudy(education.getFieldOfStudy());
        if (education.getInstitution() != null)        existing.setInstitution(education.getInstitution());
        if (education.getPassingYear() != null)        existing.setPassingYear(education.getPassingYear());
        if (education.getGradeOrPercentage() != null)  existing.setGradeOrPercentage(education.getGradeOrPercentage());
        return facultyEducationRepository.save(existing);
    }

    public void delete(Long id) {
        facultyEducationRepository.delete(getById(id));
    }

    public List<FacultyEducation> getByFacultyId(Long facultyId) {
        return facultyEducationRepository.findAll().stream()
                .filter(fe -> facultyId.equals(fe.getFacultyId()))
                .toList();
    }
}

