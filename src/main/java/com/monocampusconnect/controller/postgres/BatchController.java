package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Batch;
import com.monocampusconnect.service.postgres.BatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<Batch> create(@RequestBody Batch batch) {
        return new ResponseEntity<>(batchService.create(batch), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {
        return ResponseEntity.ok(batchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getById(id));
    }

    @GetMapping("/public-id/{publicId}")
    public ResponseEntity<Batch> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(batchService.getByPublicId(publicId));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Batch>> getByProgramId(@PathVariable Long programId) {
        return ResponseEntity.ok(batchService.getByProgramId(programId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> update(@PathVariable Long id, @RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.update(id, batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

