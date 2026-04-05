package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.NotificationDocument;
import com.monocampusconnect.repository.mongo.NotificationDocumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationDocumentRepository notificationRepository;

    public NotificationController(NotificationDocumentRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    private String currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId.toString();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        return ResponseEntity.ok(
                notificationRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(currentTenantId())
                        .stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Map<String, Object>>> unread() {
        String tenantId = currentTenantId();
        List<Map<String, Object>> items = notificationRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .filter(n -> "UNREAD".equalsIgnoreCase(n.getStatus()))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Object>> unreadCount() {
        long count = notificationRepository.countByTenantIdAndDeletedFalseAndStatus(currentTenantId(), "UNREAD");
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, Object>> markRead(@PathVariable String id) {
        NotificationDocument n = notificationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Notification not found", 404));
        if (!currentTenantId().equals(n.getTenantId())) throw new ApiException("Forbidden", 403);
        n.setStatus("READ");
        n.setUpdatedAt(new Date());
        return ResponseEntity.ok(toDto(notificationRepository.save(n)));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Map<String, Object>> readAll() {
        String tenantId = currentTenantId();
        List<NotificationDocument> items = notificationRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId);
        for (NotificationDocument n : items) {
            n.setStatus("READ");
            n.setUpdatedAt(new Date());
        }
        notificationRepository.saveAll(items);
        return ResponseEntity.ok(Map.of("updated", items.size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        NotificationDocument n = notificationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Notification not found", 404));
        if (!currentTenantId().equals(n.getTenantId())) throw new ApiException("Forbidden", 403);
        n.setDeleted(true);
        n.setDeletedAt(new Date());
        notificationRepository.save(n);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toDto(NotificationDocument n) {
        Map<String, Object> row = new java.util.LinkedHashMap<>();
        row.put("id", n.getId());
        row.put("type", n.getNotificationType());
        row.put("title", n.getTitle());
        row.put("message", n.getBody());
        row.put("createdAt", n.getCreatedAt());
        row.put("read", "READ".equalsIgnoreCase(n.getStatus()));
        return row;
    }
}
