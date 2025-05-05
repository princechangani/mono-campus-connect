package com.monocampusconnect.service;

import com.monocampusconnect.dto.ExamRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Exam;
import com.monocampusconnect.repository.ExamRepository;
import com.monocampusconnect.validator.ExamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamValidator examValidator;

    public Exam createExam(ExamRequest request) {
        // Validate exam
        examValidator.validateExam(request);

        // Check if exam code already exists
        if (examRepository.findByExamCode(request.getExamCode()).isPresent()) {
            throw new ApiException("Exam code already exists", 400);
        }

        Exam exam = new Exam();
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
            throw new ApiException("Cannot delete exam with results", 400);
        }

        examRepository.delete(exam);
    }

    public void enrollStudent(String examId, String studentId) {
        Exam exam = examRepository.findByExamCode(examId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Check if exam is in future
        if (exam.getStartDate().before(new Date())) {
            throw new ApiException("Cannot enroll in past exams", 400);
        }

        if (!exam.getEnrolledStudents().contains(studentId)) {
            exam.getEnrolledStudents().add(studentId);
            examRepository.save(exam);
        } else {
            throw new ApiException("Student already enrolled", 400);
        }
    }

    public void unenrollStudent(String examId, String studentId) {
        Exam exam = examRepository.findByExamCode(examId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        // Check if exam has started
        if (exam.getStartDate().before(new Date())) {
            throw new ApiException("Cannot unenroll from ongoing/past exams", 400);
        }

        if (exam.getEnrolledStudents().contains(studentId)) {
            exam.getEnrolledStudents().remove(studentId);
            examRepository.save(exam);
        } else {
            throw new ApiException("Student not enrolled", 400);
        }
    }

    public double calculateTotalMarks(Exam exam) {

        return 100.00;
    }

    public int getEnrolledStudentsCount(String examId) {
        Exam exam = examRepository.findByExamCode(examId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        
        return exam.getEnrolledStudents() != null ? exam.getEnrolledStudents().size() : 0;
    }

    public List<Exam> getExamsBetweenDates(Date startDate, Date endDate) {
        return examRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<Exam> searchExams(String courseCode, Exam.ExamType type, Date startDate, Date endDate) {
        if (courseCode != null && type != null) {
            List<Exam> exams = examRepository.findByCourseCodeAndType(courseCode, type);
            if (startDate != null) {
                exams = examRepository.findByCourseCodeAndStartDateAfter(courseCode, startDate);
            }
            if (endDate != null) {
                exams = examRepository.findByCourseCodeAndEndDateBefore(courseCode, endDate);
            }
            return exams;
        } else if (type != null) {
            List<Exam> exams = examRepository.findByType(type);
            if (startDate != null) {
                exams = examRepository.findByTypeAndStartDateAfter(type, startDate);
            }
            if (endDate != null) {
                exams = examRepository.findByTypeAndEndDateBefore(type, endDate);
            }
            return exams;
        } else if (courseCode != null) {
            List<Exam> exams = examRepository.findByCourseCode(courseCode);
            if (startDate != null) {
                exams = examRepository.findByCourseCodeAndStartDateAfter(courseCode, startDate);
            }
            if (endDate != null) {
                exams = examRepository.findByCourseCodeAndEndDateBefore(courseCode, endDate);
            }
            return exams;
        } else {
            List<Exam> exams = examRepository.findAll();
            if (startDate != null) {
                exams = examRepository.findByStartDateAfter(startDate);
            }
            if (endDate != null) {
                exams = examRepository.findByEndDateBefore(endDate);
            }
            return exams;
        }
    }
}
