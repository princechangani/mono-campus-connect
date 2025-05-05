package com.monocampusconnect.validator;

import com.monocampusconnect.dto.ExamRequest;
import com.monocampusconnect.exception.ApiException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class ExamValidator {

    public void validateExam(ExamRequest request) {
        // Basic validation
        validateBasicFields(request);
        
        // Date validation
        validateDates(request);
        

        // Student validation
        validateStudents(request);
    }

    private void validateBasicFields(ExamRequest request) {
        if (request.getExamCode() == null || request.getExamCode().trim().isEmpty()) {
            throw new ApiException("Exam code is required", 400);
        }

        if (request.getCourseCode() == null || request.getCourseCode().trim().isEmpty()) {
            throw new ApiException("Course code is required", 400);
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new ApiException("Exam title is required", 400);
        }

        if (request.getType() == null) {
            throw new ApiException("Exam type is required", 400);
        }
    }

    private void validateDates(ExamRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new ApiException("Start and end dates are required", 400);
        }

        if (request.getStartDate().after(request.getEndDate())) {
            throw new ApiException("Start date cannot be after end date", 400);
        }

        // Check if exam duration is reasonable (maximum 3 hours)
        long duration = ChronoUnit.HOURS.between(
                request.getStartDate().toInstant(),
                request.getEndDate().toInstant()
        );

        if (duration > 3) {
            throw new ApiException("Exam duration cannot exceed 3 hours", 400);
        }

        // Check if exam is scheduled in future
        if (request.getStartDate().before(new Date())) {
            throw new ApiException("Exam start date must be in future", 400);
        }
    }


    private void validateStudents(ExamRequest request) {
        if (request.getEnrolledStudents() == null || request.getEnrolledStudents().isEmpty()) {
            throw new ApiException("At least one student must be enrolled", 400);
        }

        if (request.getEnrolledStudents().size() > 100) {
            throw new ApiException("Maximum 100 students can be enrolled in an exam", 400);
        }
    }
}
