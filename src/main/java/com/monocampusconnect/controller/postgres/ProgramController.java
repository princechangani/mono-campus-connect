package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Program;
import com.monocampusconnect.service.postgres.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody Program program) {
        return new ResponseEntity<>(programService.create(program), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Program>> getAll() {
        return ResponseEntity.ok(programService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programService.getById(id));
    }

    @GetMapping("/public-id/{publicId}")
    public ResponseEntity<Program> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(programService.getByPublicId(publicId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Program>> getByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(programService.getByDepartmentId(departmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable Long id, @RequestBody Program program) {
        return ResponseEntity.ok(programService.update(id, program));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

