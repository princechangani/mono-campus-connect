package com.monocampusconnect.service;

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

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamRepository examRepository;

    public Result createResult(ResultRequest request) {
        if (request.getStudentId() == null || request.getExamId() == null) {
            throw new ApiException("Student ID and Exam ID are required", 400);
        }

        // Validate exam exists
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Validate if student is enrolled
        if (!exam.getEnrolledStudents().contains(request.getStudentId())) {
            throw new ApiException("Student not enrolled in this exam", 400);
        }

        Result result = new Result();
        result.setStudentId(request.getStudentId());
        result.setCourseCode(request.getCourseCode());
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
        return resultRepository.findByStudentId(studentId);
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

    private String calculateGrade(double obtainedMarks, double totalMarks) {
        double percentage = (obtainedMarks / totalMarks) * 100;
        if (percentage >= 90) return "A";
        else if (percentage >= 80) return "B";
        else if (percentage >= 70) return "C";
        else if (percentage >= 60) return "D";
        else return "F";
    }

    private String calculateStatus(double obtainedMarks, double totalMarks) {
        double percentage = (obtainedMarks / totalMarks) * 100;
        return percentage >= 40 ? "PASSED" : "FAILED";
    }

    public Result updateResult(Long id, ResultRequest request) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ApiException("Result not found", 404));

        if (request.getStudentId() != null) {
            result.setStudentId(request.getStudentId());
        }
        if (request.getExamId() != null) {
        }
        if (request.getCourseCode() != null) {
            result.setCourseCode(request.getCourseCode());
        }
        if (request.getResultDetails() != null) {
            result.setResultDetails(request.getResultDetails());
        }
        // For primitive double types, we don't need null checks
        result.setTotalMarks(request.getTotalMarks());
        result.setObtainedMarks(request.getObtainedMarks());
        result.setGrade(calculateGrade(request.getObtainedMarks(), request.getTotalMarks()));
        result.setStatus(calculateStatus(request.getObtainedMarks(), request.getTotalMarks()));
        if (request.getComments() != null) {
            result.setComments(request.getComments());
        }
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
        if (results.isEmpty()) {
            throw new ApiException("No results found for this exam", 404);
        }

        double totalMarks = 0;
        double highestMarks = Double.MIN_VALUE;
        double lowestMarks = Double.MAX_VALUE;
        int passedCount = 0;
        int totalCount = results.size();

        for (Result result : results) {
            double marks = result.getObtainedMarks();
            totalMarks += marks;
            highestMarks = Math.max(highestMarks, marks);
            lowestMarks = Math.min(lowestMarks, marks);
            if (result.getStatus().equals("PASSED")) {
                passedCount++;
            }
        }

        double averageMarks = totalMarks / totalCount;
        double passRate = ((double) passedCount / totalCount) * 100;

        return new ExamStatistics(
            averageMarks,
            highestMarks,
            lowestMarks,
            passRate,
            results.size()
        );
    }
}
