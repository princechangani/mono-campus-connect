package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.AttendanceRecordCanonical;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.repository.postgres.AttendanceRecordCanonicalRepository;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceRecordCanonicalRepository attendanceRepository;
    private final CourseCanonicalRepository courseRepository;

    public AttendanceController(AttendanceRecordCanonicalRepository attendanceRepository,
                                CourseCanonicalRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Map<String, Object>>> getByCourse(@PathVariable String courseCode) {
        UUID tenantId = currentTenantId();
        Long courseId = resolveCourseId(tenantId, courseCode, true);
        List<Map<String, Object>> records = attendanceRepository.findAll().stream()
                .filter(r -> tenantId.equals(r.getTenantId()))
                .filter(r -> courseId != null && courseId.equals(r.getCourseId()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getByStudent(@PathVariable Long studentId) {
        UUID tenantId = currentTenantId();
        List<Map<String, Object>> records = attendanceRepository.findAll().stream()
                .filter(r -> tenantId.equals(r.getTenantId()))
                .filter(r -> studentId.equals(r.getStudentId()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/student/{studentId}/course/{courseCode}")
    public ResponseEntity<List<Map<String, Object>>> getByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable String courseCode) {
        UUID tenantId = currentTenantId();
        Long courseId = resolveCourseId(tenantId, courseCode, true);
        List<Map<String, Object>> records = attendanceRepository.findAll().stream()
                .filter(r -> tenantId.equals(r.getTenantId()))
                .filter(r -> studentId.equals(r.getStudentId()))
                .filter(r -> courseId != null && courseId.equals(r.getCourseId()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(records);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Map<String, Object>> bulkCreate(@RequestBody List<Map<String, Object>> payload) {
        UUID tenantId = currentTenantId();
        int created = 0;
        List<AttendanceRecordCanonical> toSave = new ArrayList<>();
        List<String> missingCourses = new ArrayList<>();
        for (Map<String, Object> row : payload) {
            String courseCode = asString(row.get("courseCode"));
            Long studentId = asLong(row.get("studentId"));
            OffsetDateTime sessionDate = parseDate(row.get("sessionDate"));
            String status = asString(row.get("status"));
            if (courseCode == null || studentId == null || sessionDate == null) {
                throw new ApiException("courseCode, studentId, and sessionDate are required", 400);
            }
            Long courseId = resolveCourseId(tenantId, courseCode, true);
            if (courseId == null) missingCourses.add(courseCode);

            AttendanceRecordCanonical record = new AttendanceRecordCanonical();
            record.setTenantId(tenantId);
            record.setCourseId(courseId);
            record.setStudentId(studentId);
            record.setSessionDate(sessionDate);
            record.setStatus(status);
            record.setMarkedAt(OffsetDateTime.now());
            toSave.add(record);
        }
        if (!missingCourses.isEmpty()) {
            throw new ApiException("Unknown courseCode(s): " + String.join(", ", new HashSet<>(missingCourses)), 404);
        }
        attendanceRepository.saveAll(toSave);
        created = toSave.size();
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("created", created));
    }

    @GetMapping("/percentage/{studentId}/course/{courseCode}")
    public ResponseEntity<Map<String, Object>> percentage(@PathVariable Long studentId, @PathVariable String courseCode) {
        UUID tenantId = currentTenantId();
        Long courseId = resolveCourseId(tenantId, courseCode, true);
        List<AttendanceRecordCanonical> records = attendanceRepository.findAll().stream()
                .filter(r -> tenantId.equals(r.getTenantId()))
                .filter(r -> studentId.equals(r.getStudentId()))
                .filter(r -> courseId != null && courseId.equals(r.getCourseId()))
                .toList();
        long total = records.size();
        long present = records.stream().filter(r -> "PRESENT".equalsIgnoreCase(r.getStatus())).count();
        double pct = total == 0 ? 0 : (present * 100.0 / total);
        boolean below = pct < 75.0;
        return ResponseEntity.ok(Map.of(
                "percentage", Math.round(pct),
                "presentClasses", present,
                "totalClasses", total,
                "belowThreshold", below
        ));
    }

    private Map<String, Object> toDto(AttendanceRecordCanonical r) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", r.getAttendanceRecordId());
        row.put("studentId", r.getStudentId());
        row.put("courseCode", resolveCourseCode(r));
        row.put("sessionDate", r.getSessionDate());
        row.put("status", r.getStatus());
        row.put("remarks", r.getRemarks());
        return row;
    }

    private Long resolveCourseId(UUID tenantId, String courseCode, boolean strict) {
        if (courseCode == null || courseCode.isBlank()) return null;
        Optional<Long> id = courseRepository.findByTenantIdAndCodeAndIsDeletedFalse(tenantId, courseCode.toUpperCase())
                .map(CourseCanonical::getCourseId);
        if (id.isEmpty() && strict) throw new ApiException("Course not found: " + courseCode, 404);
        return id.orElse(null);
    }

    private String resolveCourseCode(AttendanceRecordCanonical r) {
        if (r.getCourseId() == null) return null;
        return courseRepository.findById(r.getCourseId())
                .map(CourseCanonical::getCode)
                .orElse(null);
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

    private static OffsetDateTime parseDate(Object val) {
        if (val == null) return null;
        if (val instanceof String s) {
            if (s.isBlank()) return null;
            // Accept ISO date or datetime
            if (s.length() == 10) return OffsetDateTime.parse(s + "T00:00:00Z");
            return OffsetDateTime.parse(s);
        }
        if (val instanceof Date d) return d.toInstant().atOffset(ZoneOffset.UTC);
        return null;
    }
}
