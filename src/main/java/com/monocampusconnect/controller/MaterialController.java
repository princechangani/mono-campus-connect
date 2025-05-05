package com.monocampusconnect.controller;

import com.monocampusconnect.dto.MaterialRequest;
import com.monocampusconnect.dto.MaterialStats;
import com.monocampusconnect.model.Material;
import com.monocampusconnect.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Material> createMaterial(
            @RequestPart("materialCode") String materialCode,
            @RequestPart("courseCode") String courseCode,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
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

        return new ResponseEntity<>(materialService.createMaterial(request, file), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<Material> getMaterial(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterial(id));
    }

/*
    @GetMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Material>> getMaterialsByCourse(
            @RequestPart("courseCode") String courseCode) {
        return ResponseEntity.ok(materialService.getMaterialsByCourse(courseCode));
    }
*/

    @GetMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Material>> getMaterialsByType(
            @RequestPart("type") String type) {
        return ResponseEntity.ok(materialService.getMaterialsByType(type));
    }

    @GetMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Material>> getMaterialsByUploader(
            @RequestPart("uploader") String uploader) {
        return ResponseEntity.ok(materialService.getMaterialsByUploader(uploader));
    }

    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Material>> getRecentMaterials(
            @RequestPart("limit") int limit) {
        return ResponseEntity.ok(materialService.getRecentMaterials(limit));
    }

/*    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Material>> getMaterialsByDateRange(
            @RequestPart("startDate") Date startDate,
            @RequestPart("endDate") Date endDate) {
        return ResponseEntity.ok(materialService.getMaterialsByDateRange(startDate, endDate));
    }*/

 /*   @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<Material>> searchMaterials(
            @RequestPart(value = "title", required = false) String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "courseCode", required = false) String courseCode,
            @RequestPart(value = "type", required = false) String type,
            @RequestPart(value = "startDate", required = false) Date startDate,
            @RequestPart(value = "endDate", required = false) Date endDate) {
        return ResponseEntity.ok(materialService.searchMaterials(title, description, courseCode, type, startDate, endDate));
    }
*/
    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaterialStats> getMaterialStats() {
        return ResponseEntity.ok(materialService.getMaterialStats());
    }

/*
    @PutMapping("/{materialCode}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable String materialCode,
            @RequestPart(value = "materialCode", required = false) String newMaterialCode,
            @RequestPart(value = "courseCode", required = false) String courseCode,
            @RequestPart(value = "title", required = false) String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "type", required = false) String type,
            @RequestPart(value = "uploadedBy", required = false) String uploadedBy,
            @RequestPart(required = false) MultipartFile file) throws IOException {
        
        MaterialRequest request = new MaterialRequest();
        request.setMaterialCode(materialCode);
        request.setCourseCode(courseCode);
        request.setTitle(title);
        request.setDescription(description);
        request.setType(type);
        request.setUploadedBy(uploadedBy);

        return ResponseEntity.ok(materialService.updateMaterial(materialCode, request, file));
    }
*/

/*    @DeleteMapping("/{materialCode}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> deleteMaterial(@PathVariable String materialCode) {
        materialService.deleteMaterial(materialCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/download/{materialCode}")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<Void> downloadMaterial(@PathVariable String materialCode) {
        materialService.incrementDownloadCount(materialCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/downloads/{materialCode}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Integer> getMaterialDownloads(@PathVariable String materialCode) {
        return ResponseEntity.ok(materialService.getDownloadCount(materialCode));
    }*/
}
