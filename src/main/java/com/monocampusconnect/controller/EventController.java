package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Announcement;
import com.monocampusconnect.repository.postgres.AnnouncementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final AnnouncementRepository announcementRepository;

    public EventController(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        UUID tenantId = currentTenantId();
        List<Map<String, Object>> items = announcementRepository.findAll().stream()
                .filter(a -> tenantId.equals(a.getTenantId()))
                .filter(a -> a.getIsDeleted() == null || !a.getIsDeleted())
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        Announcement a = new Announcement();
        a.setTenantId(currentTenantId());
        a.setTitle(asString(payload.get("title")));
        a.setContent(asString(payload.get("description")));
        a.setPostedBy(asString(payload.get("postedBy")));
        a.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(announcementRepository.save(a)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        Announcement a = announcementRepository.findById(id)
                .orElseThrow(() -> new ApiException("Event not found", 404));
        if (!tenantId.equals(a.getTenantId())) throw new ApiException("Forbidden", 403);
        if (payload.containsKey("title")) a.setTitle(asString(payload.get("title")));
        if (payload.containsKey("description")) a.setContent(asString(payload.get("description")));
        if (payload.containsKey("postedBy")) a.setPostedBy(asString(payload.get("postedBy")));
        a.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(toDto(announcementRepository.save(a)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        UUID tenantId = currentTenantId();
        Announcement a = announcementRepository.findById(id)
                .orElseThrow(() -> new ApiException("Event not found", 404));
        if (!tenantId.equals(a.getTenantId())) throw new ApiException("Forbidden", 403);
        a.setIsDeleted(Boolean.TRUE);
        a.setDeletedAt(OffsetDateTime.now());
        announcementRepository.save(a);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toDto(Announcement a) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", a.getAnnouncementId());
        row.put("title", a.getTitle());
        row.put("description", a.getContent());
        row.put("postedBy", a.getPostedBy());
        row.put("createdAt", a.getCreatedAt());
        return row;
    }

    private static String asString(Object val) {
        return val == null ? null : String.valueOf(val);
    }
}
