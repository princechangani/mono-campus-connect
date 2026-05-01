package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.ExamSchedule;
import com.monocampusconnect.service.postgres.ExamScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    public ExamScheduleController(ExamScheduleService examScheduleService) {
        this.examScheduleService = examScheduleService;
    }

    @PostMapping
    public ResponseEntity<ExamSchedule> create(@RequestBody ExamSchedule examSchedule) {
        return new ResponseEntity<>(examScheduleService.create(examSchedule), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExamSchedule>> getAll() {
        return ResponseEntity.ok(examScheduleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamSchedule> getById(@PathVariable Long id) {
        return ResponseEntity.ok(examScheduleService.getById(id));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamSchedule>> getByExamId(@PathVariable Long examId) {
        return ResponseEntity.ok(examScheduleService.getByExamId(examId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ExamSchedule>> getByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(examScheduleService.getByRoomId(roomId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamSchedule> update(@PathVariable Long id, @RequestBody ExamSchedule examSchedule) {
        return ResponseEntity.ok(examScheduleService.update(id, examSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
