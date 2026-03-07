package com.monocampusconnect.controller;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.dto.AttendancePercentage;
import com.monocampusconnect.dto.AttendanceRequest;
import com.monocampusconnect.model.Attendance;
import com.monocampusconnect.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private JwtConfig jwtConfig;

    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtConfig.extractUserId(header.substring(7));
        }
        return null;
    }

    /** POST /api/attendance/mark — mark single student */
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@Valid @RequestBody AttendanceRequest request,
                                                      HttpServletRequest httpRequest) {
        Long facultyId = extractUserId(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request, facultyId));
    }

    /** POST /api/attendance/bulk — mark entire class */
    @PostMapping("/bulk")
    public ResponseEntity<List<Attendance>> bulkMarkAttendance(@RequestBody List<@Valid AttendanceRequest> requests,
                                                                HttpServletRequest httpRequest) {
        Long facultyId = extractUserId(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.bulkMarkAttendance(requests, facultyId));
    }

    /** PUT /api/attendance/{id} — update a record */
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id,
                                                        @RequestParam Attendance.AttendanceStatus status,
                                                        @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, status, remarks));
    }

    /** GET /api/attendance/student/{studentId} */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studentId));
    }

    /** GET /api/attendance/student/{studentId}/course/{courseCode} */
    @GetMapping("/student/{studentId}/course/{courseCode}")
    public ResponseEntity<List<Attendance>> getStudentCourseAttendance(@PathVariable Long studentId,
                                                                        @PathVariable String courseCode) {
        return ResponseEntity.ok(attendanceService.getStudentCourseAttendance(studentId, courseCode));
    }

    /** GET /api/attendance/course/{courseCode} */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Attendance>> getCourseAttendance(@PathVariable String courseCode) {
        return ResponseEntity.ok(attendanceService.getCourseAttendance(courseCode));
    }

    /** GET /api/attendance/course/{courseCode}/date?date=2026-03-08 */
    @GetMapping("/course/{courseCode}/date")
    public ResponseEntity<List<Attendance>> getCourseAttendanceByDate(@PathVariable String courseCode,
                                                                       @RequestParam Date date) {
        return ResponseEntity.ok(attendanceService.getCourseAttendanceByDate(courseCode, date));
    }

    /** GET /api/attendance/percentage/{studentId}/course/{courseCode} */
    @GetMapping("/percentage/{studentId}/course/{courseCode}")
    public ResponseEntity<AttendancePercentage> getAttendancePercentage(@PathVariable Long studentId,
                                                                         @PathVariable String courseCode) {
        return ResponseEntity.ok(attendanceService.getAttendancePercentage(studentId, courseCode));
    }
}

