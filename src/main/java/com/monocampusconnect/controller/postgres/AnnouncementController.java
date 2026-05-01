package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Announcement;
import com.monocampusconnect.service.postgres.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    public ResponseEntity<Announcement> create(@RequestBody Announcement announcement) {
        return new ResponseEntity<>(announcementService.create(announcement), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAll() {
        return ResponseEntity.ok(announcementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Announcement>> getActive() {
        return ResponseEntity.ok(announcementService.getActive());
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Announcement>> getByTenantId(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(announcementService.getByTenantId(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> update(@PathVariable Long id, @RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.update(id, announcement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

