package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.ProgramCourse;
import com.monocampusconnect.service.postgres.ProgramCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program-courses")
public class ProgramCourseController {

    private final ProgramCourseService programCourseService;

    public ProgramCourseController(ProgramCourseService programCourseService) {
        this.programCourseService = programCourseService;
    }

    @PostMapping
    public ResponseEntity<ProgramCourse> create(@RequestBody ProgramCourse programCourse) {
        return new ResponseEntity<>(programCourseService.create(programCourse), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProgramCourse>> getAll() {
        return ResponseEntity.ok(programCourseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramCourse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programCourseService.getById(id));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<ProgramCourse>> getByProgramId(@PathVariable Long programId) {
        return ResponseEntity.ok(programCourseService.getByProgramId(programId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ProgramCourse>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(programCourseService.getByCourseId(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramCourse> update(@PathVariable Long id, @RequestBody ProgramCourse programCourse) {
        return ResponseEntity.ok(programCourseService.update(id, programCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programCourseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

