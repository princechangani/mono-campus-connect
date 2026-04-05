package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.model.postgres.DepartmentCanonical;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.postgres.DepartmentCanonicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseCanonicalRepository courseRepository;
    private final DepartmentCanonicalRepository departmentRepository;
    public CourseController(CourseCanonicalRepository courseRepository,
                            DepartmentCanonicalRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        UUID tenantId = currentTenantId();
        List<CourseCanonical> courses = courseRepository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(tenantId);

        Map<Long, String> deptNames = new HashMap<>();
        List<Map<String, Object>> response = new ArrayList<>();
        for (CourseCanonical c : courses) {
            String deptName = null;
            if (c.getDepartmentId() != null) {
                deptName = deptNames.computeIfAbsent(c.getDepartmentId(), id ->
                        departmentRepository.findById(id).map(DepartmentCanonical::getName).orElse(null));
            }
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", c.getCourseId());
            row.put("courseCode", c.getCode());
            row.put("courseName", c.getName());
            row.put("credits", c.getCredits());
            row.put("semester", c.getSemesterNumber());
            row.put("department", deptName);
            row.put("instructor", c.getInstructorName());
            row.put("subjectType", c.getSubjectType());
            row.put("category", c.getCategory());
            response.add(row);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        String code = String.valueOf(payload.getOrDefault("courseCode", "")).trim().toUpperCase();
        String name = String.valueOf(payload.getOrDefault("courseName", "")).trim();
        if (code.isEmpty() || name.isEmpty()) throw new ApiException("courseCode and courseName are required", 400);

        CourseCanonical course = new CourseCanonical();
        course.setTenantId(tenantId);
        course.setCode(code);
        course.setName(name);
        course.setCredits(asInteger(payload.get("credits")));
        course.setSemesterNumber(asInteger(payload.get("semester")));
        course.setSubjectType(asString(payload.get("subjectType")));
        course.setCategory(asString(payload.get("category")));
        course.setInstructorName(asString(payload.get("instructor")));
        course.setCourseType(asString(payload.get("subjectType")));
        course.setIsActive(Boolean.TRUE);
        course.setCreatedAt(OffsetDateTime.now());

        String dept = asString(payload.get("department"));
        if (dept != null && !dept.isBlank()) {
            DepartmentCanonical department = findOrCreateDepartment(tenantId, dept);
            course.setDepartmentId(department.getDepartmentId());
        }

        CourseCanonical saved = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        CourseCanonical course = courseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course not found", 404));
        if (!tenantId.equals(course.getTenantId())) throw new ApiException("Forbidden", 403);

        if (payload.containsKey("courseCode")) course.setCode(asString(payload.get("courseCode")).toUpperCase());
        if (payload.containsKey("courseName")) course.setName(asString(payload.get("courseName")));
        if (payload.containsKey("credits")) course.setCredits(asInteger(payload.get("credits")));
        if (payload.containsKey("semester")) course.setSemesterNumber(asInteger(payload.get("semester")));
        if (payload.containsKey("subjectType")) course.setSubjectType(asString(payload.get("subjectType")));
        if (payload.containsKey("category")) course.setCategory(asString(payload.get("category")));
        if (payload.containsKey("instructor")) course.setInstructorName(asString(payload.get("instructor")));
        if (payload.containsKey("department")) {
            String dept = asString(payload.get("department"));
            if (dept != null && !dept.isBlank()) {
                DepartmentCanonical department = findOrCreateDepartment(tenantId, dept);
                course.setDepartmentId(department.getDepartmentId());
            } else {
                course.setDepartmentId(null);
            }
        }
        course.setUpdatedAt(OffsetDateTime.now());

        return ResponseEntity.ok(toDto(courseRepository.save(course)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        UUID tenantId = currentTenantId();
        CourseCanonical course = courseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course not found", 404));
        if (!tenantId.equals(course.getTenantId())) throw new ApiException("Forbidden", 403);
        course.setIsDeleted(Boolean.TRUE);
        course.setDeletedAt(OffsetDateTime.now());
        courseRepository.save(course);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private DepartmentCanonical findOrCreateDepartment(UUID tenantId, String dept) {
        String normalized = dept.trim();
        Optional<DepartmentCanonical> existing = departmentRepository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .filter(d -> normalized.equalsIgnoreCase(d.getName()) || normalized.equalsIgnoreCase(d.getCode()))
                .findFirst();
        if (existing.isPresent()) return existing.get();

        DepartmentCanonical created = new DepartmentCanonical();
        created.setTenantId(tenantId);
        created.setName(normalized);
        created.setCode(normalized.length() <= 6 ? normalized.toUpperCase() : normalized.substring(0, 6).toUpperCase());
        created.setCreatedAt(OffsetDateTime.now());
        return departmentRepository.save(created);
    }

    private Map<String, Object> toDto(CourseCanonical c) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", c.getCourseId());
        row.put("courseCode", c.getCode());
        row.put("courseName", c.getName());
        row.put("credits", c.getCredits());
        row.put("semester", c.getSemesterNumber());
        row.put("subjectType", c.getSubjectType());
        row.put("category", c.getCategory());
        row.put("instructor", c.getInstructorName());
        return row;
    }

    private static String asString(Object val) {
        return val == null ? null : String.valueOf(val);
    }

    private static Integer asInteger(Object val) {
        if (val == null) return null;
        if (val instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(String.valueOf(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
