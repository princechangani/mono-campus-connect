package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.ExamCanonical;
import com.monocampusconnect.model.postgres.Mark;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.postgres.ExamCanonicalRepository;
import com.monocampusconnect.repository.postgres.MarkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

    private final MarkRepository markRepository;
    private final ExamCanonicalRepository examRepository;
    private final CourseCanonicalRepository courseRepository;

    public ResultsController(MarkRepository markRepository,
                             ExamCanonicalRepository examRepository,
                             CourseCanonicalRepository courseRepository) {
        this.markRepository = markRepository;
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        UUID tenantId = currentTenantId();
        List<Map<String, Object>> items = markRepository.findAll().stream()
                .filter(m -> tenantId.equals(m.getTenantId()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getByStudent(@PathVariable Long studentId) {
        UUID tenantId = currentTenantId();
        List<Map<String, Object>> items = markRepository.findAll().stream()
                .filter(m -> tenantId.equals(m.getTenantId()))
                .filter(m -> studentId.equals(m.getStudentId()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        Mark mark = new Mark();
        mark.setTenantId(currentTenantId());
        mark.setStudentId(asLong(payload.get("studentId")));
        mark.setExamId(asLong(payload.get("examId")));
        mark.setMarksObtained(asDecimal(payload.get("obtainedMarks")));
        mark.setTotalMarks(asDecimal(payload.get("totalMarks")));
        mark.setGrade(asString(payload.get("grade")));
        mark.setStatus(asString(payload.getOrDefault("status", "PENDING")));
        if (mark.getExamId() == null) throw new ApiException("examId is required", 400);
        ExamCanonical exam = examRepository.findById(mark.getExamId())
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        mark.setCourseId(exam.getCourseId());
        Mark saved = markRepository.save(mark);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Mark mark = markRepository.findById(id).orElseThrow(() -> new ApiException("Result not found", 404));
        if (!currentTenantId().equals(mark.getTenantId())) throw new ApiException("Forbidden", 403);

        if (payload.containsKey("studentId")) mark.setStudentId(asLong(payload.get("studentId")));
        if (payload.containsKey("examId")) mark.setExamId(asLong(payload.get("examId")));
        if (payload.containsKey("obtainedMarks")) mark.setMarksObtained(asDecimal(payload.get("obtainedMarks")));
        if (payload.containsKey("totalMarks")) mark.setTotalMarks(asDecimal(payload.get("totalMarks")));
        if (payload.containsKey("grade")) mark.setGrade(asString(payload.get("grade")));
        if (payload.containsKey("status")) mark.setStatus(asString(payload.get("status")));
        if (payload.containsKey("examId") && mark.getExamId() != null) {
            examRepository.findById(mark.getExamId()).ifPresent(exam -> mark.setCourseId(exam.getCourseId()));
        }
        return ResponseEntity.ok(toDto(markRepository.save(mark)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Mark mark = markRepository.findById(id).orElseThrow(() -> new ApiException("Result not found", 404));
        if (!currentTenantId().equals(mark.getTenantId())) throw new ApiException("Forbidden", 403);
        mark.setStatus(asString(payload.getOrDefault("status", "PUBLISHED")));
        return ResponseEntity.ok(toDto(markRepository.save(mark)));
    }

    private Map<String, Object> toDto(Mark mark) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", mark.getMarkId());
        row.put("studentId", mark.getStudentId());
        row.put("examId", mark.getExamId());
        row.put("obtainedMarks", mark.getMarksObtained());
        row.put("totalMarks", mark.getTotalMarks());
        row.put("grade", mark.getGrade());
        row.put("status", mark.getStatus());

        if (mark.getExamId() != null) {
            examRepository.findById(mark.getExamId()).ifPresent(exam -> {
                row.put("examCode", exam.getExamCode());
                if (exam.getCourseId() != null) {
                    courseRepository.findById(exam.getCourseId()).ifPresent(course ->
                            row.put("courseCode", course.getCode()));
                }
            });
        }
        return row;
    }

    private static String asString(Object val) {
        return val == null ? null : String.valueOf(val);
    }

    private static Long asLong(Object val) {
        if (val == null) return null;
        if (val instanceof Number n) return n.longValue();
        try {
            return Long.parseLong(String.valueOf(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static java.math.BigDecimal asDecimal(Object val) {
        if (val == null) return null;
        if (val instanceof java.math.BigDecimal d) return d;
        if (val instanceof Number n) return java.math.BigDecimal.valueOf(n.doubleValue());
        try {
            return new java.math.BigDecimal(String.valueOf(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
