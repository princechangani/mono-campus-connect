package com.monocampusconnect.controller.mongo;

import com.monocampusconnect.dto.mongo.NotificationDocumentRequest;
import com.monocampusconnect.dto.mongo.NotificationDocumentResponse;
import com.monocampusconnect.service.mongo.NotificationDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/canonical/mongo/tenants/{tenantId}/notifications")
public class NotificationDocumentController {

    private final NotificationDocumentService notificationService;

    public NotificationDocumentController(NotificationDocumentService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationDocumentResponse> create(
            @PathVariable String tenantId,
            @RequestParam(required = false) Long createdBy,
            @Valid @RequestBody NotificationDocumentRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(request, createdBy));
    }

    @GetMapping
    public ResponseEntity<List<NotificationDocumentResponse>> getAll(@PathVariable String tenantId) {
        return ResponseEntity.ok(notificationService.getAllByTenant(tenantId));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<NotificationDocumentResponse> getByPublicId(
            @PathVariable String tenantId,
            @PathVariable String publicId
    ) {
        return ResponseEntity.ok(notificationService.getByPublicId(tenantId, publicId));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<NotificationDocumentResponse> update(
            @PathVariable String tenantId,
            @PathVariable String publicId,
            @Valid @RequestBody NotificationDocumentRequest request
    ) {
        request.setTenantId(tenantId);
        return ResponseEntity.ok(notificationService.update(tenantId, publicId, request));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable String tenantId,
            @PathVariable String publicId,
            @RequestParam(required = false) Long deletedBy
    ) {
        notificationService.delete(tenantId, publicId, deletedBy);
        return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
    }
}

