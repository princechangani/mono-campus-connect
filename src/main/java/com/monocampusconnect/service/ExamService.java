package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.ExamRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Exam;
import com.monocampusconnect.repository.ExamRepository;
import com.monocampusconnect.validator.ExamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamValidator examValidator;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public Exam createExam(ExamRequest request) {
        // Validate exam
        examValidator.validateExam(request);

        // Check if exam code already exists
        if (examRepository.findByExamCode(request.getExamCode()).isPresent()) {
            throw new ApiException("Exam code already exists", 400);
        }

        Exam exam = new Exam();
        exam.setTenantId(currentTenant());
        exam.setExamCode(request.getExamCode());
        exam.setCourseCode(request.getCourseCode());
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setStartDate(request.getStartDate());
        exam.setEndDate(request.getEndDate());
        exam.setType(request.getType());
        exam.setEnrolledStudents(request.getEnrolledStudents());

        return examRepository.save(exam);
    }

    public Exam getExam(String examCode) {
        return examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new ApiException("Exam not found", 404));
    }

    public List<Exam> getExamsByCourse(String courseCode) {
        return examRepository.findByCourseCode(courseCode);
    }

    public List<Exam> getAllExams() {
        return examRepository.findByTenantId(currentTenant());
    }

    public List<Exam> getExamsByType(Exam.ExamType type) {
        return examRepository.findByType(type);
    }

    public List<Exam> getExamsByStudent(String studentId) {
        return examRepository.findByEnrolledStudentsContains(studentId);
    }

    public List<Exam> getUpcomingExams(String studentId) {
        return examRepository.findByEnrolledStudentsContainsAndStartDateAfter(studentId, new Date());
    }

    public List<Exam> getPastExams(String studentId) {
        return examRepository.findByEnrolledStudentsContainsAndEndDateBefore(studentId, new Date());
    }

    public Exam updateExam(Long id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Validate exam
        examValidator.validateExam(request);

        // Check if exam code is being changed and if new code exists
        if (!exam.getExamCode().equals(request.getExamCode()) && 
            examRepository.findByExamCode(request.getExamCode()).isPresent()) {
            throw new ApiException("Exam code already exists", 400);
        }

        exam.setExamCode(request.getExamCode());
        exam.setCourseCode(request.getCourseCode());
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setStartDate(request.getStartDate());
        exam.setEndDate(request.getEndDate());
        exam.setType(request.getType());
        exam.setEnrolledStudents(request.getEnrolledStudents());

        return examRepository.save(exam);
    }



    public void deleteExam(String examCode) {
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Check if exam has any results
        if (exam.getResults() != null && !exam.getResults().isEmpty()) {
            throw new ApiException("Cannot delete exam with existing results", 400);
        }

        examRepository.delete(exam);
    }

    public void enrollStudent(String examCode, String studentId) {
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Check if exam is in future
        if (exam.getStartDate().before(new Date())) {
            throw new ApiException("Cannot enroll in past exams", 400);
        }

        if (exam.getEnrolledStudents().contains(studentId)) {
            throw new ApiException("Student already enrolled", 400);
        }

        exam.getEnrolledStudents().add(studentId);
        examRepository.save(exam);
    }

    public void unenrollStudent(String examCode, String studentId) {
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Check if exam has started
        if (exam.getStartDate().before(new Date())) {
            throw new ApiException("Cannot unenroll from ongoing/past exams", 400);
        }

        if (!exam.getEnrolledStudents().contains(studentId)) {
            throw new ApiException("Student not enrolled", 400);
        }

        exam.getEnrolledStudents().remove(studentId);
        examRepository.save(exam);
    }

    public int getEnrolledStudentsCount(String examCode) {
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        return exam.getEnrolledStudents() != null ? exam.getEnrolledStudents().size() : 0;
    }

    public List<Exam> getExamsBetweenDates(Date startDate, Date endDate) {
        return examRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<Exam> searchExams(String courseCode, Exam.ExamType type, Date startDate, Date endDate) {
        if (courseCode != null && type != null) {
            return examRepository.findByCourseCodeAndType(courseCode, type);
        } else if (type != null) {
            if (startDate != null) return examRepository.findByTypeAndStartDateAfter(type, startDate);
            if (endDate != null) return examRepository.findByTypeAndEndDateBefore(type, endDate);
            return examRepository.findByType(type);
        } else if (courseCode != null) {
            if (startDate != null) return examRepository.findByCourseCodeAndStartDateAfter(courseCode, startDate);
            if (endDate != null) return examRepository.findByCourseCodeAndEndDateBefore(courseCode, endDate);
            return examRepository.findByCourseCode(courseCode);
        } else {
            if (startDate != null) return examRepository.findByStartDateAfter(startDate);
            if (endDate != null) return examRepository.findByEndDateBefore(endDate);
            return getAllExams();
        }
    }
}
