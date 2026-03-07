package com.monocampusconnect.controller;

import com.monocampusconnect.dto.TimetableRequest;
import com.monocampusconnect.model.TimetableEntry;
import com.monocampusconnect.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    /** POST /api/timetable — ADMIN creates an entry */
    @PostMapping
    public ResponseEntity<TimetableEntry> createEntry(@Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createEntry(request));
    }

    /** GET /api/timetable — full timetable for the tenant */
    @GetMapping
    public ResponseEntity<List<TimetableEntry>> getFullTimetable() {
        return ResponseEntity.ok(timetableService.getFullTimetable());
    }

    /** GET /api/timetable/semester/{semester} */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<TimetableEntry>> getTimetableBySemester(@PathVariable String semester) {
        return ResponseEntity.ok(timetableService.getTimetableBySemester(semester));
    }

    /** GET /api/timetable/faculty/{facultyId} */
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<TimetableEntry>> getTimetableByFaculty(@PathVariable String facultyId) {
        return ResponseEntity.ok(timetableService.getTimetableByFaculty(facultyId));
    }

    /** GET /api/timetable/day/{dayOfWeek} */
    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<List<TimetableEntry>> getTimetableByDay(@PathVariable String dayOfWeek) {
        return ResponseEntity.ok(timetableService.getTimetableByDay(dayOfWeek));
    }

    /** GET /api/timetable/course/{courseCode} */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<TimetableEntry>> getTimetableByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(timetableService.getTimetableByCourse(courseCode));
    }

    /** PUT /api/timetable/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<TimetableEntry> updateEntry(@PathVariable Long id,
                                                       @Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.ok(timetableService.updateEntry(id, request));
    }

    /** DELETE /api/timetable/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEntry(@PathVariable Long id) {
        timetableService.deleteEntry(id);
        return ResponseEntity.ok(Map.of("message", "Timetable entry deleted successfully"));
    }
}

