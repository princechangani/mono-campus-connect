package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.MaterialDocument;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.mongo.MaterialDocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialDocumentRepository materialRepository;
    private final CourseCanonicalRepository courseRepository;

    public MaterialController(MaterialDocumentRepository materialRepository,
                              CourseCanonicalRepository courseRepository) {
        this.materialRepository = materialRepository;
        this.courseRepository = courseRepository;
    }

    private String currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId.toString();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        String tenantId = currentTenantId();
        List<Map<String, Object>> items = materialRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/recent/{count}")
    public ResponseEntity<List<Map<String, Object>>> recent(@PathVariable int count) {
        String tenantId = currentTenantId();
        List<Map<String, Object>> items = materialRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .limit(Math.max(0, count))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> create(
            @RequestPart("materialCode") String materialCode,
            @RequestPart("courseCode") String courseCode,
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("type") String type,
            @RequestPart(value = "uploadedBy", required = false) String uploadedBy,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        MaterialDocument material = new MaterialDocument();
        material.setTenantId(currentTenantId());
        material.setMaterialCode(materialCode);
        CourseCanonical course = resolveCourseOrThrow(courseCode);
        material.setCourseId(course.getCourseId());
        material.setTitle(title);
        material.setDescription(description);
        material.setMaterialType(type);
        material.setUploadedBy(uploadedBy);
        material.setUploadedDate(new Date());

        if (file != null) {
            material.setFileUrl(file.getOriginalFilename());
            material.setFileSizeBytes(file.getSize());
            material.setMimeType(file.getContentType());
        }
        material.setCreatedAt(new Date());
        MaterialDocument saved = materialRepository.save(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        String tenantId = currentTenantId();
        MaterialDocument material = materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found", 404));
        if (!tenantId.equals(material.getTenantId())) throw new ApiException("Forbidden", 403);
        material.setDeleted(true);
        material.setDeletedAt(new Date());
        materialRepository.save(material);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toDto(MaterialDocument m) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", m.getId());
        row.put("materialCode", m.getMaterialCode());
        row.put("courseCode", resolveCourseCode(m));
        row.put("title", m.getTitle());
        row.put("description", m.getDescription());
        row.put("type", m.getMaterialType());
        row.put("uploadedBy", m.getUploadedBy());
        row.put("uploadedDate", m.getUploadedDate() != null ? m.getUploadedDate() : m.getCreatedAt());
        row.put("downloadCount", m.getDownloadCount());
        return row;
    }

    private Optional<CourseCanonical> resolveCourse(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) return Optional.empty();
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) return Optional.empty();
        return courseRepository.findByTenantIdAndCodeAndIsDeletedFalse(tenantId, courseCode.toUpperCase());
    }

    private CourseCanonical resolveCourseOrThrow(String courseCode) {
        return resolveCourse(courseCode)
                .orElseThrow(() -> new ApiException("Course not found: " + courseCode, 404));
    }

    private String resolveCourseCode(MaterialDocument m) {
        if (m.getCourseId() == null) return null;
        return courseRepository.findById(m.getCourseId())
                .map(CourseCanonical::getCode)
                .orElse(null);
    }
}
