package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.FacultyEducation;
import com.monocampusconnect.service.postgres.FacultyEducationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty-education")
public class FacultyEducationController {

    private final FacultyEducationService facultyEducationService;

    public FacultyEducationController(FacultyEducationService facultyEducationService) {
        this.facultyEducationService = facultyEducationService;
    }

    @PostMapping
    public ResponseEntity<FacultyEducation> create(@RequestBody FacultyEducation education) {
        return new ResponseEntity<>(facultyEducationService.create(education), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FacultyEducation>> getAll() {
        return ResponseEntity.ok(facultyEducationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyEducation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyEducationService.getById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<FacultyEducation>> getByFacultyId(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyEducationService.getByFacultyId(facultyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyEducation> update(@PathVariable Long id, @RequestBody FacultyEducation education) {
        return ResponseEntity.ok(facultyEducationService.update(id, education));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facultyEducationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
