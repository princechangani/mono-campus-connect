package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.FeeStructure;
import com.monocampusconnect.service.postgres.FeeStructureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fee-structures")
public class FeeStructureController {

    private final FeeStructureService feeStructureService;

    public FeeStructureController(FeeStructureService feeStructureService) {
        this.feeStructureService = feeStructureService;
    }

    @PostMapping
    public ResponseEntity<FeeStructure> create(@RequestBody FeeStructure feeStructure) {
        return new ResponseEntity<>(feeStructureService.create(feeStructure), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FeeStructure>> getAll() {
        return ResponseEntity.ok(feeStructureService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeStructure> getById(@PathVariable Long id) {
        return ResponseEntity.ok(feeStructureService.getById(id));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<FeeStructure>> getByProgramId(@PathVariable Long programId) {
        return ResponseEntity.ok(feeStructureService.getByProgramId(programId));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<FeeStructure>> getByTenantId(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(feeStructureService.getByTenantId(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeStructure> update(@PathVariable Long id, @RequestBody FeeStructure feeStructure) {
        return ResponseEntity.ok(feeStructureService.update(id, feeStructure));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feeStructureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
