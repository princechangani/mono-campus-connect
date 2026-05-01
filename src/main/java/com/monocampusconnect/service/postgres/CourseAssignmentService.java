package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.CourseAssignment;
import com.monocampusconnect.repository.postgres.CourseAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseAssignmentService {

    private final CourseAssignmentRepository courseAssignmentRepository;

    public CourseAssignmentService(CourseAssignmentRepository courseAssignmentRepository) {
        this.courseAssignmentRepository = courseAssignmentRepository;
    }

    public CourseAssignment create(CourseAssignment assignment) {
        return courseAssignmentRepository.save(assignment);
    }

    public List<CourseAssignment> getAll() {
        return courseAssignmentRepository.findAll();
    }

    public CourseAssignment getById(Long id) {
        return courseAssignmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course assignment not found", 404));
    }

    public CourseAssignment update(Long id, CourseAssignment assignment) {
        CourseAssignment existing = getById(id);
        if (assignment.getCourseId() != null) existing.setCourseId(assignment.getCourseId());
        if (assignment.getFacultyId() != null) existing.setFacultyId(assignment.getFacultyId());
        if (assignment.getBatchId() != null) existing.setBatchId(assignment.getBatchId());
        if (assignment.getAcademicYearId() != null) existing.setAcademicYearId(assignment.getAcademicYearId());
        if (assignment.getSemesterNumber() != null) existing.setSemesterNumber(assignment.getSemesterNumber());
        if (assignment.getSection() != null) existing.setSection(assignment.getSection());
        return courseAssignmentRepository.save(existing);
    }

    public void delete(Long id) {
        courseAssignmentRepository.delete(getById(id));
    }

    public List<CourseAssignment> getByFacultyId(Long facultyId) {
        return courseAssignmentRepository.findAll().stream()
                .filter(ca -> facultyId.equals(ca.getFacultyId()))
                .toList();
    }

    public List<CourseAssignment> getByCourseId(Long courseId) {
        return courseAssignmentRepository.findAll().stream()
                .filter(ca -> courseId.equals(ca.getCourseId()))
                .toList();
    }
}

