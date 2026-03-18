package com.monocampusconnect.controller.mongo;

import com.monocampusconnect.dto.canonical.mongo.MaterialDocumentRequest;
import com.monocampusconnect.dto.canonical.mongo.MaterialDocumentResponse;
import com.monocampusconnect.service.mongo.MaterialDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/canonical/mongo/tenants/{tenantId}/materials")
public class MaterialDocumentController {

    private final MaterialDocumentService materialService;

    public MaterialDocumentController(MaterialDocumentService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialDocumentResponse> create(
            @PathVariable String tenantId,
            @RequestParam(required = false) Long createdBy,
            @Valid @RequestBody MaterialDocumentRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.create(request, createdBy));
    }

    @GetMapping
    public ResponseEntity<List<MaterialDocumentResponse>> getAll(
            @PathVariable String tenantId,
            @RequestParam(required = false) Long courseId
    ) {
        return ResponseEntity.ok(materialService.getAllByTenant(tenantId, courseId));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<MaterialDocumentResponse> getByPublicId(
            @PathVariable String tenantId,
            @PathVariable String publicId
    ) {
        return ResponseEntity.ok(materialService.getByPublicId(tenantId, publicId));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<MaterialDocumentResponse> update(
            @PathVariable String tenantId,
            @PathVariable String publicId,
            @Valid @RequestBody MaterialDocumentRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.ok(materialService.update(tenantId, publicId, request));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable String tenantId,
            @PathVariable String publicId,
            @RequestParam(required = false) Long deletedBy
    ) {
        materialService.delete(tenantId, publicId, deletedBy);
        return ResponseEntity.ok(Map.of("message", "Material deleted successfully"));
    }
}
