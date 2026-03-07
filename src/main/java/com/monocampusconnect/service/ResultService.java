package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.ExamStatistics;
import com.monocampusconnect.dto.ResultRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Exam;
import com.monocampusconnect.model.Result;
import com.monocampusconnect.repository.ExamRepository;
import com.monocampusconnect.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamRepository examRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public Result createResult(ResultRequest request) {
        if (request.getStudentId() == null || request.getExamId() == null) {
            throw new ApiException("Student ID and Exam ID are required", 400);
        }
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        if (!exam.getEnrolledStudents().contains(request.getStudentId())) {
            throw new ApiException("Student not enrolled in this exam", 400);
        }
        Result result = new Result();
        result.setTenantId(currentTenant());
        result.setStudentId(request.getStudentId());
        result.setExamCode(exam.getExamCode());
        result.setCourseCode(request.getCourseCode());
        result.setExam(exam);
        result.setResultDetails(request.getResultDetails());
        result.setTotalMarks(request.getTotalMarks());
        result.setObtainedMarks(request.getObtainedMarks());
        result.setGrade(calculateGrade(request.getObtainedMarks(), request.getTotalMarks()));
        result.setStatus(calculateStatus(request.getObtainedMarks(), request.getTotalMarks()));
        result.setResultDate(new Date());
        result.setComments(request.getComments());
        result.setCreatedAt(new Date());
        result.setUpdatedAt(new Date());
        return resultRepository.save(result);
    }

    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ApiException("Result not found", 404));
    }

    public Result getResultByStudentAndExam(String studentId, String examCode) {
        return resultRepository.findByStudentIdAndExamCode(studentId, examCode)
                .orElseThrow(() -> new ApiException("Result not found", 404));
    }

    public List<Result> getResultsByStudent(String studentId) {
        return resultRepository.findByTenantIdAndStudentId(currentTenant(), studentId);
    }

    public List<Result> getResultsByExam(String examCode) {
        return resultRepository.findByExamCode(examCode);
    }

    public List<Result> getResultsByCourse(String courseCode) {
        return resultRepository.findByCourseCode(courseCode);
    }

    public List<Result> getResultsByStatus(String status) {
        return resultRepository.findByStatus(status);
    }

    public List<Result> getAllResults() {
        return resultRepository.findByTenantId(currentTenant());
    }

    private String calculateGrade(double obtainedMarks, double totalMarks) {
        double pct = (obtainedMarks / totalMarks) * 100;
        if (pct >= 90) return "A+";
        else if (pct >= 80) return "A";
        else if (pct >= 70) return "B";
        else if (pct >= 60) return "C";
        else if (pct >= 50) return "D";
        else return "F";
    }

    private String calculateStatus(double obtainedMarks, double totalMarks) {
        return ((obtainedMarks / totalMarks) * 100) >= 40 ? "PASSED" : "FAILED";
    }

    public Result updateResult(Long id, ResultRequest request) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ApiException("Result not found", 404));
        if (!currentTenant().equals(result.getTenantId()))
            throw new ApiException("Result not found in this college", 404);
        if (request.getStudentId() != null) result.setStudentId(request.getStudentId());
        if (request.getCourseCode() != null) result.setCourseCode(request.getCourseCode());
        if (request.getResultDetails() != null) result.setResultDetails(request.getResultDetails());
        result.setTotalMarks(request.getTotalMarks());
        result.setObtainedMarks(request.getObtainedMarks());
        result.setGrade(calculateGrade(request.getObtainedMarks(), request.getTotalMarks()));
        result.setStatus(calculateStatus(request.getObtainedMarks(), request.getTotalMarks()));
        if (request.getComments() != null) result.setComments(request.getComments());
        result.setUpdatedAt(new Date());
        return resultRepository.save(result);
    }

    public void updateResultStatus(Long id, String status) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ApiException("Result not found", 404));
        result.setStatus(status);
        resultRepository.save(result);
    }

    public ExamStatistics getExamStatistics(Long examId) {
        List<Result> results = resultRepository.findByExamId(examId);
        if (results.isEmpty()) throw new ApiException("No results found for this exam", 404);
        double total = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        int passed = 0;
        for (Result r : results) {
            double m = r.getObtainedMarks();
            total += m;
            highest = Math.max(highest, m);
            lowest = Math.min(lowest, m);
            if ("PASSED".equals(r.getStatus())) passed++;
        }
        return new ExamStatistics(total / results.size(), highest, lowest,
                ((double) passed / results.size()) * 100, results.size());
    }
}
