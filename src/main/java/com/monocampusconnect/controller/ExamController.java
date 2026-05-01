package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.model.postgres.ExamCanonical;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.postgres.ExamCanonicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamCanonicalRepository examRepository;
    private final CourseCanonicalRepository courseRepository;

    public ExamController(ExamCanonicalRepository examRepository, CourseCanonicalRepository courseRepository) {
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
        List<Map<String, Object>> items = examRepository.findAll().stream()
                .filter(e -> tenantId.equals(e.getTenantId()))
                .filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        ExamCanonical exam = new ExamCanonical();
        exam.setTenantId(tenantId);
        exam.setExamCode(asString(payload.get("examCode")));
        exam.setExamType(asString(payload.get("type")));
        exam.setName(asString(payload.get("title")));
        String courseCode = asString(payload.get("courseCode"));
        CourseCanonical course = resolveCourse(tenantId, courseCode);
        exam.setCourseId(course.getCourseId());
        exam.setDescription(asString(payload.get("description")));
        exam.setStartDate(parseLocalDate(payload.get("startDate")));
        exam.setEndDate(parseLocalDate(payload.get("endDate")));
        exam.setCreatedAt(OffsetDateTime.now());
        ExamCanonical saved = examRepository.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        ExamCanonical exam = examRepository.findById(id)
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        if (!tenantId.equals(exam.getTenantId())) throw new ApiException("Forbidden", 403);

        if (payload.containsKey("examCode")) exam.setExamCode(asString(payload.get("examCode")));
        if (payload.containsKey("type")) exam.setExamType(asString(payload.get("type")));
        if (payload.containsKey("title")) exam.setName(asString(payload.get("title")));
        if (payload.containsKey("courseCode")) {
            String courseCode = asString(payload.get("courseCode"));
            CourseCanonical course = resolveCourse(tenantId, courseCode);
            exam.setCourseId(course.getCourseId());
        }
        if (payload.containsKey("description")) exam.setDescription(asString(payload.get("description")));
        if (payload.containsKey("startDate")) exam.setStartDate(parseLocalDate(payload.get("startDate")));
        if (payload.containsKey("endDate")) exam.setEndDate(parseLocalDate(payload.get("endDate")));
        exam.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(toDto(examRepository.save(exam)));
    }

    @DeleteMapping("/{examCode}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String examCode) {
        UUID tenantId = currentTenantId();
        ExamCanonical exam = examRepository.findByExamCodeAndTenantId(examCode, tenantId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));
        exam.setIsDeleted(Boolean.TRUE);
        exam.setDeletedAt(OffsetDateTime.now());
        examRepository.save(exam);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    @PostMapping("/{examCode}/enroll")
    public ResponseEntity<Map<String, Object>> enroll(@PathVariable String examCode, @RequestParam Long studentId) {
        UUID tenantId = currentTenantId();
        ExamCanonical exam = examRepository.findByExamCodeAndTenantId(examCode, tenantId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        Set<String> ids = new LinkedHashSet<>();
        if (exam.getEnrolledStudentIds() != null && !exam.getEnrolledStudentIds().isBlank()) {
            ids.addAll(Arrays.asList(exam.getEnrolledStudentIds().split(",")));
        }
        ids.add(String.valueOf(studentId));
        exam.setEnrolledStudentIds(String.join(",", ids));
        exam.setUpdatedAt(OffsetDateTime.now());
        examRepository.save(exam);

        return ResponseEntity.ok(toDto(exam));
    }

    @DeleteMapping("/{examCode}/enroll")
    public ResponseEntity<Map<String, Object>> unenroll(@PathVariable String examCode, @RequestParam Long studentId) {
        UUID tenantId = currentTenantId();
        ExamCanonical exam = examRepository.findByExamCodeAndTenantId(examCode, tenantId)
                .orElseThrow(() -> new ApiException("Exam not found", 404));

        Set<String> ids = new LinkedHashSet<>();
        if (exam.getEnrolledStudentIds() != null && !exam.getEnrolledStudentIds().isBlank()) {
            ids.addAll(Arrays.asList(exam.getEnrolledStudentIds().split(",")));
        }
        ids.remove(String.valueOf(studentId));
        exam.setEnrolledStudentIds(String.join(",", ids));
        exam.setUpdatedAt(OffsetDateTime.now());
        examRepository.save(exam);

        return ResponseEntity.ok(toDto(exam));
    }

    private Map<String, Object> toDto(ExamCanonical exam) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", exam.getExamId());
        row.put("examCode", exam.getExamCode());
        row.put("type", exam.getExamType());
        row.put("title", exam.getName());
        row.put("courseCode", resolveCourseCode(exam));
        row.put("description", exam.getDescription());
        row.put("startDate", toDate(exam.getStartDate()));
        row.put("endDate", toDate(exam.getEndDate()));
        row.put("enrolledStudents", parseIds(exam.getEnrolledStudentIds()));
        return row;
    }

    private CourseCanonical resolveCourse(UUID tenantId, String courseCode) {
        if (courseCode == null || courseCode.isBlank()) throw new ApiException("courseCode is required", 400);
        return courseRepository.findByTenantIdAndCodeAndIsDeletedFalse(tenantId, courseCode.toUpperCase())
                .orElseThrow(() -> new ApiException("Course not found: " + courseCode, 404));
    }

    private String resolveCourseCode(ExamCanonical exam) {
        if (exam.getCourseId() == null) return null;
        return courseRepository.findById(exam.getCourseId())
                .map(CourseCanonical::getCode)
                .orElse(null);
    }

    private static LocalDate parseLocalDate(Object val) {
        if (val == null) return null;
        if (val instanceof String s) {
            if (s.isBlank()) return null;
            // Accept ISO date or datetime strings
            return LocalDate.parse(s.substring(0, 10));
        }
        if (val instanceof Date d) {
            return d.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        }
        return null;
    }

    private static Date toDate(LocalDate date) {
        if (date == null) return null;
        return Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    private static List<Long> parseIds(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private static String asString(Object val) {
        return val == null ? null : String.valueOf(val);
    }
}
