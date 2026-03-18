package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.AttendanceSession;
import com.monocampusconnect.repository.postgres.AttendanceSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceSessionService {

    private final AttendanceSessionRepository attendanceSessionRepository;

    public AttendanceSessionService(AttendanceSessionRepository attendanceSessionRepository) {
        this.attendanceSessionRepository = attendanceSessionRepository;
    }

    public AttendanceSession create(AttendanceSession session) {
        return attendanceSessionRepository.save(session);
    }

    public List<AttendanceSession> getAll() {
        return attendanceSessionRepository.findAll();
    }

    public AttendanceSession getById(Long id) {
        return attendanceSessionRepository.findById(id)
                .orElseThrow(() -> new ApiException("Attendance session not found", 404));
    }

    public AttendanceSession update(Long id, AttendanceSession session) {
        AttendanceSession existing = getById(id);
        if (session.getCourseAssignmentId() != null) existing.setCourseAssignmentId(session.getCourseAssignmentId());
        if (session.getSlotId() != null) existing.setSlotId(session.getSlotId());
        if (session.getRoomId() != null) existing.setRoomId(session.getRoomId());
        if (session.getSessionDate() != null) existing.setSessionDate(session.getSessionDate());
        if (session.getTopicCovered() != null) existing.setTopicCovered(session.getTopicCovered());
        return attendanceSessionRepository.save(existing);
    }

    public void delete(Long id) {
        attendanceSessionRepository.delete(getById(id));
    }

    public List<AttendanceSession> getByCourseAssignmentId(Long courseAssignmentId) {
        return attendanceSessionRepository.findAll().stream()
                .filter(s -> courseAssignmentId.equals(s.getCourseAssignmentId()))
                .toList();
    }

    public List<AttendanceSession> getByConductedBy(Long facultyId) {
        return attendanceSessionRepository.findAll().stream()
                .filter(s -> facultyId.equals(s.getConductedBy()))
                .toList();
    }
}

