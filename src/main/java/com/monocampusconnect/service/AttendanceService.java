package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.AttendancePercentage;
import com.monocampusconnect.dto.AttendanceRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Attendance;
import com.monocampusconnect.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private static final double THRESHOLD = 75.0;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    /** Mark attendance for a single student */
    @Transactional
    public Attendance markAttendance(AttendanceRequest request, Long facultyId) {
        UUID tenantId = currentTenant();

        // Check if already marked
        attendanceRepository
                .findByTenantIdAndStudentIdAndCourseCodeAndSessionDate(
                        tenantId, request.getStudentId(), request.getCourseCode(), request.getSessionDate())
                .ifPresent(existing -> {
                    throw new ApiException(
                            "Attendance already marked for student " + request.getStudentId()
                                    + " on this date for course " + request.getCourseCode(), 409);
                });

        Attendance attendance = new Attendance();
        attendance.setTenantId(tenantId);
        attendance.setStudentId(request.getStudentId());
        attendance.setCourseCode(request.getCourseCode());
        attendance.setSessionDate(request.getSessionDate());
        attendance.setStatus(request.getStatus());
        attendance.setMarkedBy(facultyId);
        attendance.setRemarks(request.getRemarks());
        return attendanceRepository.save(attendance);
    }

    /** Bulk mark attendance for an entire class on a date */
    @Transactional
    public List<Attendance> bulkMarkAttendance(List<AttendanceRequest> requests, Long facultyId) {
        return requests.stream()
                .map(req -> markAttendance(req, facultyId))
                .toList();
    }

    /** Update existing attendance record */
    @Transactional
    public Attendance updateAttendance(Long id, Attendance.AttendanceStatus status, String remarks) {
        UUID tenantId = currentTenant();
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Attendance record not found", 404));
        if (!tenantId.equals(attendance.getTenantId()))
            throw new ApiException("Attendance record not found", 404);
        attendance.setStatus(status);
        if (remarks != null) attendance.setRemarks(remarks);
        return attendanceRepository.save(attendance);
    }

    /** Get all attendance records for a student */
    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByTenantIdAndStudentId(currentTenant(), studentId);
    }

    /** Get attendance for a student in a specific course */
    public List<Attendance> getStudentCourseAttendance(Long studentId, String courseCode) {
        return attendanceRepository.findByTenantIdAndStudentIdAndCourseCode(currentTenant(), studentId, courseCode);
    }

    /** Get all attendance for a course on a specific date */
    public List<Attendance> getCourseAttendanceByDate(String courseCode, Date date) {
        return attendanceRepository.findByTenantIdAndCourseCodeAndSessionDate(currentTenant(), courseCode, date);
    }

    /** Get all attendance for a course */
    public List<Attendance> getCourseAttendance(String courseCode) {
        return attendanceRepository.findByTenantIdAndCourseCode(currentTenant(), courseCode);
    }

    /** Calculate attendance percentage for a student in a course */
    public AttendancePercentage getAttendancePercentage(Long studentId, String courseCode) {
        UUID tenantId = currentTenant();
        long total = attendanceRepository.countTotal(tenantId, studentId, courseCode);
        if (total == 0) {
            return new AttendancePercentage(studentId, courseCode, 0, 0, 0.0, true);
        }
        long present = attendanceRepository.countPresent(tenantId, studentId, courseCode);
        double percentage = ((double) present / total) * 100;
        return new AttendancePercentage(studentId, courseCode, total, present,
                Math.round(percentage * 100.0) / 100.0, percentage < THRESHOLD);
    }
}

