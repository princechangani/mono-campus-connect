package com.monocampusconnect.controller;

import com.monocampusconnect.dto.ExamStatistics;
import com.monocampusconnect.dto.ResultRequest;
import com.monocampusconnect.model.Result;
import com.monocampusconnect.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    /** POST /api/results */
    @PostMapping
    public ResponseEntity<Result> createResult(@Valid @RequestBody ResultRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.createResult(request));
    }

    /** GET /api/results */
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResults());
    }

    /** GET /api/results/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResult(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    /** GET /api/results/student/{studentId} */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Result>> getResultsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(resultService.getResultsByStudent(studentId));
    }

    /** GET /api/results/exam/{examCode} */
    @GetMapping("/exam/{examCode}")
    public ResponseEntity<List<Result>> getResultsByExam(@PathVariable String examCode) {
        return ResponseEntity.ok(resultService.getResultsByExam(examCode));
    }

    /** GET /api/results/course/{courseCode} */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Result>> getResultsByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(resultService.getResultsByCourse(courseCode));
    }

    /** GET /api/results/status/{status} — ADMIN only */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Result>> getResultsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(resultService.getResultsByStatus(status));
    }

    /** GET /api/results/statistics/{examId} */
    @GetMapping("/statistics/{examId}")
    public ResponseEntity<ExamStatistics> getExamStatistics(@PathVariable Long examId) {
        return ResponseEntity.ok(resultService.getExamStatistics(examId));
    }

    /** PUT /api/results/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable Long id,
                                               @Valid @RequestBody ResultRequest request) {
        return ResponseEntity.ok(resultService.updateResult(id, request));
    }

    /** PUT /api/results/{id}/status */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateResultStatus(@PathVariable Long id,
                                                                   @RequestParam String status) {
        resultService.updateResultStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "Status updated successfully"));
    }
}
