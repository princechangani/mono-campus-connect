package com.monocampusconnect.controller;

import com.monocampusconnect.dto.MaterialRequest;
import com.monocampusconnect.dto.MaterialStats;
import com.monocampusconnect.model.Material;
import com.monocampusconnect.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /** POST /api/materials — multipart upload */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Material> createMaterial(
            @RequestPart("materialCode") String materialCode,
            @RequestPart("courseCode") String courseCode,
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("type") String type,
            @RequestPart("uploadedBy") String uploadedBy,
            @RequestPart("file") MultipartFile file) throws IOException {

        MaterialRequest request = new MaterialRequest();
        request.setMaterialCode(materialCode);
        request.setCourseCode(courseCode);
        request.setTitle(title);
        request.setDescription(description);
        request.setType(type);
        request.setUploadedBy(uploadedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.createMaterial(request, file));
    }

    /** GET /api/materials — all materials for tenant */
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    /** GET /api/materials/{publicId} — download/view + increments count */
    @GetMapping("/{publicId}")
    public ResponseEntity<Material> getMaterial(@PathVariable UUID publicId) {
        return ResponseEntity.ok(materialService.getMaterialByPublicId(publicId));
    }

    /** GET /api/materials/course/{courseCode} */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Material>> getMaterialsByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(materialService.getMaterialsByCourse(courseCode));
    }

    /** GET /api/materials/type/{type} — ADMIN only */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Material>> getMaterialsByType(@PathVariable String type) {
        return ResponseEntity.ok(materialService.getMaterialsByType(type));
    }

    /** GET /api/materials/uploader/{uploader} */
    @GetMapping("/uploader/{uploader}")
    public ResponseEntity<List<Material>> getMaterialsByUploader(@PathVariable String uploader) {
        return ResponseEntity.ok(materialService.getMaterialsByUploader(uploader));
    }

    /** GET /api/materials/recent/{limit} */
    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<Material>> getRecentMaterials(@PathVariable int limit) {
        return ResponseEntity.ok(materialService.getRecentMaterials(limit));
    }

    /** GET /api/materials/stats */
    @GetMapping("/stats")
    public ResponseEntity<MaterialStats> getMaterialStats() {
        return ResponseEntity.ok(materialService.getMaterialStats());
    }

    /** PUT /api/materials/{publicId} — update with optional new file */
    @PutMapping(value = "/{publicId}", consumes = "multipart/form-data")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable UUID publicId,
            @RequestPart("courseCode") String courseCode,
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("type") String type,
            @RequestPart(value = "uploadedBy", required = false) String uploadedBy,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        MaterialRequest request = new MaterialRequest();
        request.setCourseCode(courseCode);
        request.setTitle(title);
        request.setDescription(description);
        request.setType(type);
        request.setUploadedBy(uploadedBy);
        return ResponseEntity.ok(materialService.updateMaterialByPublicId(publicId, request, file));
    }

    /** DELETE /api/materials/{publicId} */
    @DeleteMapping("/{publicId}")
    public ResponseEntity<Map<String, String>> deleteMaterial(@PathVariable UUID publicId) {
        materialService.deleteMaterialByPublicId(publicId);
        return ResponseEntity.ok(Map.of("message", "Material deleted successfully"));
    }
}
