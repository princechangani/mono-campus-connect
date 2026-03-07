package com.monocampusconnect.controller;

import com.monocampusconnect.dto.ExamRequest;
import com.monocampusconnect.model.Exam;
import com.monocampusconnect.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    /** POST /api/exams — create exam (FACULTY/ADMIN) */
    @PostMapping
    public ResponseEntity<Exam> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(request));
    }

    /** GET /api/exams — list all exams for current tenant */
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    /** GET /api/exams/{examCode} */
    @GetMapping("/{examCode}")
    public ResponseEntity<Exam> getExam(@PathVariable String examCode) {
        return ResponseEntity.ok(examService.getExam(examCode));
    }

    /** GET /api/exams/course/{courseCode} */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseCode));
    }

    /** GET /api/exams/type/{type} — ADMIN only */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Exam>> getExamsByType(@PathVariable String type) {
        return ResponseEntity.ok(examService.getExamsByType(Exam.ExamType.valueOf(type.toUpperCase())));
    }

    /** GET /api/exams/student/{studentId} */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Exam>> getExamsByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getExamsByStudent(studentId));
    }

    /** GET /api/exams/student/{studentId}/upcoming */
    @GetMapping("/student/{studentId}/upcoming")
    public ResponseEntity<List<Exam>> getUpcomingExams(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getUpcomingExams(studentId));
    }

    /** GET /api/exams/student/{studentId}/past */
    @GetMapping("/student/{studentId}/past")
    public ResponseEntity<List<Exam>> getPastExams(@PathVariable String studentId) {
        return ResponseEntity.ok(examService.getPastExams(studentId));
    }

    /** GET /api/exams/{examCode}/students-count */
    @GetMapping("/{examCode}/students-count")
    public ResponseEntity<Integer> getEnrolledStudentsCount(@PathVariable String examCode) {
        return ResponseEntity.ok(examService.getEnrolledStudentsCount(examCode));
    }

    /** PUT /api/exams/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.updateExam(id, request));
    }

    /** DELETE /api/exams/{examCode} */
    @DeleteMapping("/{examCode}")
    public ResponseEntity<Map<String, String>> deleteExam(@PathVariable String examCode) {
        examService.deleteExam(examCode);
        return ResponseEntity.ok(Map.of("message", "Exam deleted successfully"));
    }

    /** POST /api/exams/{examCode}/enroll?studentId=... */
    @PostMapping("/{examCode}/enroll")
    public ResponseEntity<Map<String, String>> enrollStudent(@PathVariable String examCode,
                                                              @RequestParam String studentId) {
        examService.enrollStudent(examCode, studentId);
        return ResponseEntity.ok(Map.of("message", "Student enrolled successfully"));
    }

    /** DELETE /api/exams/{examCode}/enroll?studentId=... */
    @DeleteMapping("/{examCode}/enroll")
    public ResponseEntity<Map<String, String>> unenrollStudent(@PathVariable String examCode,
                                                                @RequestParam String studentId) {
        examService.unenrollStudent(examCode, studentId);
        return ResponseEntity.ok(Map.of("message", "Student unenrolled successfully"));
    }

    /** GET /api/exams/search */
    @GetMapping("/search")
    public ResponseEntity<List<Exam>> searchExams(
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        Exam.ExamType examType = type != null ? Exam.ExamType.valueOf(type.toUpperCase()) : null;
        return ResponseEntity.ok(examService.searchExams(courseCode, examType, startDate, endDate));
    }

    /** GET /api/exams/between-dates */
    @GetMapping("/between-dates")
    public ResponseEntity<List<Exam>> getExamsBetweenDates(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return ResponseEntity.ok(examService.getExamsBetweenDates(startDate, endDate));
    }
}
