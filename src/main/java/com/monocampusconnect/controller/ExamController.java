package com.monocampusconnect.controller;

import com.monocampusconnect.dto.ExamRequest;
import com.monocampusconnect.model.Exam;
import com.monocampusconnect.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Exam> createExam(
            @RequestPart("examCode") String examCode,
            @RequestPart("courseCode") String courseCode,
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("startDate") Date startDate,
            @RequestPart("endDate") Date endDate,
            @RequestPart("type") Exam.ExamType type,
            @RequestPart(value = "enrolledStudents", required = false) List<String> enrolledStudents,
            @RequestPart("file") MultipartFile file) {
        
        ExamRequest request = new ExamRequest();
        request.setExamCode(examCode);
        request.setCourseCode(courseCode);
        request.setTitle(title);
        request.setDescription(description);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setType(type);
        request.setEnrolledStudents(enrolledStudents);

        return new ResponseEntity<>(examService.createExam(request), HttpStatus.CREATED);
    }

    @GetMapping("/{examCode}")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<Exam> getExam(@PathVariable String examCode) {
        return ResponseEntity.ok(examService.getExam(examCode));
    }

    @GetMapping("/course/{courseCode}")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseCode));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Exam>> getExamsByType(@PathVariable String type) {
        return ResponseEntity.ok(examService.getExamsByType(Exam.ExamType.valueOf(type.toUpperCase())));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getExamsByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getExamsByStudent(studentId));
    }

    @GetMapping("/upcoming/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getUpcomingExams(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getUpcomingExams(studentId));
    }

    @GetMapping("/past/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getPastExams(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getPastExams(studentId));
    }

    @GetMapping("/total-marks/{examCode}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Double> getTotalMarks(@PathVariable String examCode) {
        Exam exam = examService.getExam(examCode);
        double totalMarks = examService.calculateTotalMarks(exam);
        return ResponseEntity.ok(totalMarks);
    }

    @GetMapping("/students-count/{examCode}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<Integer> getEnrolledStudentsCount(@PathVariable String examCode) {
        return ResponseEntity.ok(examService.getEnrolledStudentsCount(examCode));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Exam> updateExam(@PathVariable Long examId, @RequestBody ExamRequest request) {
        try {
            return ResponseEntity.ok(examService.updateExam(examId, request));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid exam ID format");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> deleteExam(@PathVariable String id) {
        examService.deleteExam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> enrollStudent(
            @RequestPart("examId") String examId,
            @RequestPart("studentId") String studentId) {
        examService.enrollStudent(examId, studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> unenrollStudent(
            @RequestParam("examId") String examId,
            @RequestParam("studentId") String studentId) {
        examService.unenrollStudent(examId, studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Exam>> getExamsBetweenDates(
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate) {
        return ResponseEntity.ok(examService.getExamsBetweenDates(startDate, endDate));
    }

    @GetMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Exam>> searchExams(
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) Exam.ExamType type,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return ResponseEntity.ok(examService.searchExams(courseCode, type, startDate, endDate));
    }
}
