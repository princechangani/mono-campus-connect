package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.CourseAssignment;
import com.monocampusconnect.service.postgres.CourseAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-assignments")
public class CourseAssignmentController {

    private final CourseAssignmentService courseAssignmentService;

    public CourseAssignmentController(CourseAssignmentService courseAssignmentService) {
        this.courseAssignmentService = courseAssignmentService;
    }

    @PostMapping
    public ResponseEntity<CourseAssignment> create(@RequestBody CourseAssignment assignment) {
        return new ResponseEntity<>(courseAssignmentService.create(assignment), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseAssignment>> getAll() {
        return ResponseEntity.ok(courseAssignmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseAssignment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseAssignmentService.getById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<CourseAssignment>> getByFacultyId(@PathVariable Long facultyId) {
        return ResponseEntity.ok(courseAssignmentService.getByFacultyId(facultyId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseAssignment>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseAssignmentService.getByCourseId(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseAssignment> update(@PathVariable Long id, @RequestBody CourseAssignment assignment) {
        return ResponseEntity.ok(courseAssignmentService.update(id, assignment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
