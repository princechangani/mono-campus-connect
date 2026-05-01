package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Faculty;
import com.monocampusconnect.service.postgres.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> create(@RequestBody Faculty faculty) {
        return new ResponseEntity<>(facultyService.create(faculty), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAll() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getById(id));
    }

    @GetMapping("/public-id/{publicId}")
    public ResponseEntity<Faculty> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(facultyService.getByPublicId(publicId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Faculty>> getByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(facultyService.getByDepartmentId(departmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> update(@PathVariable Long id, @RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.update(id, faculty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
