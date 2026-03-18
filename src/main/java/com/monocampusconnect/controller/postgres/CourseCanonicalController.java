package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.dto.canonical.postgres.CourseCanonicalRequest;
import com.monocampusconnect.dto.canonical.postgres.CourseCanonicalResponse;
import com.monocampusconnect.service.postgres.CourseCanonicalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/canonical/postgres/tenants/{tenantId}/courses")
public class CourseCanonicalController {

    private final CourseCanonicalService courseService;

    public CourseCanonicalController(CourseCanonicalService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseCanonicalResponse> create(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CourseCanonicalRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CourseCanonicalResponse>> getAll(
            @PathVariable UUID tenantId,
            @RequestParam(required = false) Long departmentId
    ) {
        return ResponseEntity.ok(courseService.getAllByTenant(tenantId, departmentId));
    }

    @GetMapping("/{coursePublicId}")
    public ResponseEntity<CourseCanonicalResponse> getByPublicId(
            @PathVariable UUID tenantId,
            @PathVariable UUID coursePublicId
    ) {
        return ResponseEntity.ok(courseService.getByPublicId(tenantId, coursePublicId));
    }

    @PutMapping("/{coursePublicId}")
    public ResponseEntity<CourseCanonicalResponse> update(
            @PathVariable UUID tenantId,
            @PathVariable UUID coursePublicId,
            @Valid @RequestBody CourseCanonicalRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.ok(courseService.update(tenantId, coursePublicId, request));
    }

    @DeleteMapping("/{coursePublicId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable UUID tenantId,
            @PathVariable UUID coursePublicId
    ) {
        courseService.delete(tenantId, coursePublicId);
        return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
    }
}

