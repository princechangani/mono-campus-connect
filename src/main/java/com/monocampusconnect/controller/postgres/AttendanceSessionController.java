package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.AttendanceSession;
import com.monocampusconnect.service.postgres.AttendanceSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-sessions")
public class AttendanceSessionController {

    private final AttendanceSessionService attendanceSessionService;

    public AttendanceSessionController(AttendanceSessionService attendanceSessionService) {
        this.attendanceSessionService = attendanceSessionService;
    }

    @PostMapping
    public ResponseEntity<AttendanceSession> create(@RequestBody AttendanceSession session) {
        return new ResponseEntity<>(attendanceSessionService.create(session), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceSession>> getAll() {
        return ResponseEntity.ok(attendanceSessionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceSession> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceSessionService.getById(id));
    }

    @GetMapping("/course-assignment/{courseAssignmentId}")
    public ResponseEntity<List<AttendanceSession>> getByCourseAssignmentId(@PathVariable Long courseAssignmentId) {
        return ResponseEntity.ok(attendanceSessionService.getByCourseAssignmentId(courseAssignmentId));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<AttendanceSession>> getByConductedBy(@PathVariable Long facultyId) {
        return ResponseEntity.ok(attendanceSessionService.getByConductedBy(facultyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceSession> update(@PathVariable Long id, @RequestBody AttendanceSession session) {
        return ResponseEntity.ok(attendanceSessionService.update(id, session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceSessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
