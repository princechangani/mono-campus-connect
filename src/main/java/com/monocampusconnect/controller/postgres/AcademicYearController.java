package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.AcademicYear;
import com.monocampusconnect.service.postgres.AcademicYearService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    public ResponseEntity<AcademicYear> create(@RequestBody AcademicYear academicYear) {
        return new ResponseEntity<>(academicYearService.create(academicYear), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AcademicYear>> getAll() {
        return ResponseEntity.ok(academicYearService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYear> getById(@PathVariable Long id) {
        return ResponseEntity.ok(academicYearService.getById(id));
    }

    @GetMapping("/public-id/{publicId}")
    public ResponseEntity<AcademicYear> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(academicYearService.getByPublicId(publicId));
    }

    @GetMapping("/current")
    public ResponseEntity<AcademicYear> getCurrentAcademicYear() {
        return ResponseEntity.ok(academicYearService.getCurrentAcademicYear());
    }

    @GetMapping("/label/{label}")
    public ResponseEntity<AcademicYear> getByLabel(@PathVariable String label) {
        return ResponseEntity.ok(academicYearService.getByLabel(label));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYear> update(@PathVariable Long id, @RequestBody AcademicYear academicYear) {
        return ResponseEntity.ok(academicYearService.update(id, academicYear));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        academicYearService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

