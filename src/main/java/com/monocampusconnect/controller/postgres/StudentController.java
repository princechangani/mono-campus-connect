package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Student;
import com.monocampusconnect.service.postgres.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return new ResponseEntity<>(studentService.create(student), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping("/public-id/{publicId}")
    public ResponseEntity<Student> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(studentService.getByPublicId(publicId));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<Student>> getByBatchId(@PathVariable Long batchId) {
        return ResponseEntity.ok(studentService.getByBatchId(batchId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Student>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(studentService.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

