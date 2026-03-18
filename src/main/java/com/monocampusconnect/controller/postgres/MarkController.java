package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Mark;
import com.monocampusconnect.service.postgres.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping
    public ResponseEntity<Mark> create(@RequestBody Mark mark) {
        return new ResponseEntity<>(markService.create(mark), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Mark>> getAll() {
        return ResponseEntity.ok(markService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mark> getById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Mark>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(markService.getByStudentId(studentId));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<Mark>> getByExamId(@PathVariable Long examId) {
        return ResponseEntity.ok(markService.getByExamId(examId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mark> update(@PathVariable Long id, @RequestBody Mark mark) {
        return ResponseEntity.ok(markService.update(id, mark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

