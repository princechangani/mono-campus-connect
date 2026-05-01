package com.monocampusconnect.service.postgres;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Student;
import com.monocampusconnect.repository.postgres.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public Student create(Student student) {
        student.setTenantId(currentTenantId());
        if (student.getStudentPublicId() == null) {
            student.setStudentPublicId(UUID.randomUUID());
        }
        return studentRepository.save(student);
    }

    public List<Student> getAll() {
        UUID tenantId = currentTenantId();
        return studentRepository.findAll().stream()
                .filter(s -> tenantId.equals(s.getTenantId()))
                .toList();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .filter(s -> currentTenantId().equals(s.getTenantId()))
                .orElseThrow(() -> new ApiException("Student not found", 404));
    }

    public Student getByPublicId(UUID publicId) {
        return studentRepository.findAll().stream()
                .filter(s -> currentTenantId().equals(s.getTenantId()) && publicId.equals(s.getStudentPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Student not found", 404));
    }

    public Student update(Long id, Student student) {
        Student existing = getById(id);
        if (student.getUserId() != null) existing.setUserId(student.getUserId());
        if (student.getBatchId() != null) existing.setBatchId(student.getBatchId());
        if (student.getEnrollmentNumber() != null) existing.setEnrollmentNumber(student.getEnrollmentNumber());
        if (student.getEnrollmentDate() != null) existing.setEnrollmentDate(student.getEnrollmentDate());
        if (student.getCurrentSemester() != null) existing.setCurrentSemester(student.getCurrentSemester());
        if (student.getStatus() != null) existing.setStatus(student.getStatus());
        return studentRepository.save(existing);
    }

    public void delete(Long id) {
        Student student = getById(id);
        studentRepository.delete(student);
    }

    public List<Student> getByBatchId(Long batchId) {
        return studentRepository.findAll().stream()
                .filter(s -> currentTenantId().equals(s.getTenantId()) && batchId.equals(s.getBatchId()))
                .toList();
    }

    public List<Student> getByStatus(String status) {
        return studentRepository.findAll().stream()
                .filter(s -> currentTenantId().equals(s.getTenantId()) && status.equals(s.getStatus()))
                .toList();
    }
}

