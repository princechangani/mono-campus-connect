package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.ProgramCourse;
import com.monocampusconnect.repository.postgres.ProgramCourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramCourseService {

    private final ProgramCourseRepository programCourseRepository;

    public ProgramCourseService(ProgramCourseRepository programCourseRepository) {
        this.programCourseRepository = programCourseRepository;
    }

    /**
     * Create a new program course assignment
     */
    public ProgramCourse create(ProgramCourse programCourse) {
        return programCourseRepository.save(programCourse);
    }

    /**
     * Get all program courses
     */
    public List<ProgramCourse> getAll() {
        return programCourseRepository.findAll();
    }

    /**
     * Get program course by ID
     */
    public ProgramCourse getById(Long id) {
        return programCourseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Program course not found", 404));
    }

    /**
     * Update program course assignment
     */
    public ProgramCourse update(Long id, ProgramCourse programCourse) {
        ProgramCourse existing = getById(id);
        if (programCourse.getProgramId() != null) existing.setProgramId(programCourse.getProgramId());
        if (programCourse.getCourseId() != null) existing.setCourseId(programCourse.getCourseId());
        if (programCourse.getSemesterNumber() != null) existing.setSemesterNumber(programCourse.getSemesterNumber());
        if (programCourse.getIsElective() != null) existing.setIsElective(programCourse.getIsElective());
        if (programCourse.getIsMandatory() != null) existing.setIsMandatory(programCourse.getIsMandatory());
        return programCourseRepository.save(existing);
    }

    /**
     * Delete program course assignment
     */
    public void delete(Long id) {
        ProgramCourse programCourse = getById(id);
        programCourseRepository.delete(programCourse);
    }

    /**
     * Get courses by program
     */
    public List<ProgramCourse> getByProgramId(Long programId) {
        return programCourseRepository.findAll().stream()
                .filter(pc -> programId.equals(pc.getProgramId()))
                .toList();
    }

    /**
     * Get programs by course
     */
    public List<ProgramCourse> getByCourseId(Long courseId) {
        return programCourseRepository.findAll().stream()
                .filter(pc -> courseId.equals(pc.getCourseId()))
                .toList();
    }

    /**
     * Get courses by program and semester
     */
    public List<ProgramCourse> getByProgramAndSemester(Long programId, Integer semester) {
        return programCourseRepository.findAll().stream()
                .filter(pc -> programId.equals(pc.getProgramId()) && semester.equals(pc.getSemesterNumber()))
                .toList();
    }
}

