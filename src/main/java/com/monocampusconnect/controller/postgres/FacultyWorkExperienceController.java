package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.FacultyWorkExperience;
import com.monocampusconnect.service.postgres.FacultyWorkExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty-work-experience")
public class FacultyWorkExperienceController {

    private final FacultyWorkExperienceService facultyWorkExperienceService;

    public FacultyWorkExperienceController(FacultyWorkExperienceService facultyWorkExperienceService) {
        this.facultyWorkExperienceService = facultyWorkExperienceService;
    }

    @PostMapping
    public ResponseEntity<FacultyWorkExperience> create(@RequestBody FacultyWorkExperience experience) {
        return new ResponseEntity<>(facultyWorkExperienceService.create(experience), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FacultyWorkExperience>> getAll() {
        return ResponseEntity.ok(facultyWorkExperienceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyWorkExperience> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyWorkExperienceService.getById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<FacultyWorkExperience>> getByFacultyId(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyWorkExperienceService.getByFacultyId(facultyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyWorkExperience> update(@PathVariable Long id, @RequestBody FacultyWorkExperience experience) {
        return ResponseEntity.ok(facultyWorkExperienceService.update(id, experience));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facultyWorkExperienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
