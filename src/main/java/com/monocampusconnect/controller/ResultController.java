package com.monocampusconnect.controller;

import com.monocampusconnect.dto.ExamStatistics;
import com.monocampusconnect.dto.ResultRequest;
import com.monocampusconnect.dto.ResultStats;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Result;
import com.monocampusconnect.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Result> createResult(
            @RequestPart("studentId") String studentId,
            @RequestPart("examId") Long examId,
            @RequestPart("marks") Double marks,
            @RequestPart("grade") String grade,
            @RequestPart(value = "comments", required = false) String comments) {
        
        ResultRequest request = new ResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setObtainedMarks(marks);
        request.setGrade(grade);
        request.setComments(comments);
        
        return new ResponseEntity<>(resultService.createResult(request), HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Result> updateResult(
            @RequestPart("id") Long id,
            @RequestPart(value = "studentId", required = false) String studentId,
            @RequestPart(value = "examId", required = false) Long examId,
            @RequestPart(value = "marks", required = false) Double marks,
            @RequestPart(value = "grade", required = false) String grade,
            @RequestPart(value = "comments", required = false) String comments) {
        
        ResultRequest request = new ResultRequest();
        request.setStudentId(studentId);
        request.setExamId(examId);
        request.setObtainedMarks(marks);
        request.setGrade(grade);
        request.setComments(comments);
        
        return ResponseEntity.ok(resultService.updateResult(id, request));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<Result> getResult(@RequestPart("id") Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Result>> getResultsByStudent(@RequestPart("studentId") String studentId) {
        return ResponseEntity.ok(resultService.getResultsByStudent(studentId));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Result>> getResultsByExam(@RequestPart("examId") String examId) {
        return ResponseEntity.ok(resultService.getResultsByExam(examId));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ExamStatistics> getExamStatistics(@RequestPart("examId") String examId) {
        return ResponseEntity.ok(resultService.getExamStatistics(examId));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Result>> getResultsByCourse(@RequestPart("courseCode") String courseCode) {
        return ResponseEntity.ok(resultService.getResultsByCourse(courseCode));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Result>> getResultsByStatus(@RequestPart("status") String status) {
        return ResponseEntity.ok(resultService.getResultsByStatus(status));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> updateResultStatus(@PathVariable Long id, @RequestParam String status) {
        resultService.updateResultStatus(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
