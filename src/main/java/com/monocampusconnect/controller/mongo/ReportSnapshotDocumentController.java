package com.monocampusconnect.controller.mongo;

import com.monocampusconnect.model.mongo.ReportSnapshotDocument;
import com.monocampusconnect.service.mongo.ReportSnapshotDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents/report-snapshots")
public class ReportSnapshotDocumentController {

    private final ReportSnapshotDocumentService reportSnapshotDocumentService;

    public ReportSnapshotDocumentController(ReportSnapshotDocumentService reportSnapshotDocumentService) {
        this.reportSnapshotDocumentService = reportSnapshotDocumentService;
    }

    @PostMapping
    public ResponseEntity<ReportSnapshotDocument> create(@RequestBody ReportSnapshotDocument snapshot) {
        return new ResponseEntity<>(reportSnapshotDocumentService.create(snapshot), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReportSnapshotDocument>> getAll() {
        return ResponseEntity.ok(reportSnapshotDocumentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportSnapshotDocument> getById(@PathVariable String id) {
        return ResponseEntity.ok(reportSnapshotDocumentService.getById(id));
    }

    @GetMapping("/public-id")
    public ResponseEntity<ReportSnapshotDocument> getByPublicId(
            @RequestParam String tenantId,
            @RequestParam String publicId) {
        return ResponseEntity.ok(reportSnapshotDocumentService.getByPublicId(tenantId, publicId));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ReportSnapshotDocument>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(reportSnapshotDocumentService.getByTenantId(tenantId));
    }

    @GetMapping("/report-type/{reportType}")
    public ResponseEntity<List<ReportSnapshotDocument>> getByReportType(@PathVariable String reportType) {
        return ResponseEntity.ok(reportSnapshotDocumentService.getByReportType(reportType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportSnapshotDocument> update(@PathVariable String id, @RequestBody ReportSnapshotDocument snapshot) {
        return ResponseEntity.ok(reportSnapshotDocumentService.update(id, snapshot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reportSnapshotDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

