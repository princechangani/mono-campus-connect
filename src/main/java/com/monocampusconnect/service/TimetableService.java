package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.TimetableRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.TimetableEntry;
import com.monocampusconnect.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    private TimetableEntry buildEntry(TimetableRequest request, UUID tenantId) {
        TimetableEntry entry = new TimetableEntry();
        entry.setTenantId(tenantId);
        entry.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        entry.setTimeSlot(request.getTimeSlot());
        entry.setCourseCode(request.getCourseCode());
        entry.setCourseName(request.getCourseName());
        entry.setFacultyId(request.getFacultyId());
        entry.setFacultyName(request.getFacultyName());
        entry.setRoomNumber(request.getRoomNumber());
        entry.setSemester(request.getSemester());
        return entry;
    }

    @Transactional
    public TimetableEntry createEntry(TimetableRequest request) {
        return timetableRepository.save(buildEntry(request, currentTenant()));
    }

    public List<TimetableEntry> getFullTimetable() {
        return timetableRepository.findByTenantId(currentTenant());
    }

    public List<TimetableEntry> getTimetableBySemester(String semester) {
        return timetableRepository.findByTenantIdAndSemester(currentTenant(), semester);
    }

    public List<TimetableEntry> getTimetableByFaculty(String facultyId) {
        return timetableRepository.findByTenantIdAndFacultyId(currentTenant(), facultyId);
    }

    public List<TimetableEntry> getTimetableByDay(String dayOfWeek) {
        return timetableRepository.findByTenantIdAndDayOfWeek(currentTenant(), dayOfWeek.toUpperCase());
    }

    public List<TimetableEntry> getTimetableByCourse(String courseCode) {
        return timetableRepository.findByTenantIdAndCourseCode(currentTenant(), courseCode);
    }

    @Transactional
    public TimetableEntry updateEntry(Long id, TimetableRequest request) {
        UUID tenantId = currentTenant();
        TimetableEntry entry = timetableRepository.findById(id)
                .orElseThrow(() -> new ApiException("Timetable entry not found", 404));
        if (!tenantId.equals(entry.getTenantId()))
            throw new ApiException("Timetable entry not found in this college", 404);
        entry.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        entry.setTimeSlot(request.getTimeSlot());
        entry.setCourseCode(request.getCourseCode());
        entry.setCourseName(request.getCourseName());
        entry.setFacultyId(request.getFacultyId());
        entry.setFacultyName(request.getFacultyName());
        entry.setRoomNumber(request.getRoomNumber());
        entry.setSemester(request.getSemester());
        return timetableRepository.save(entry);
    }

    @Transactional
    public void deleteEntry(Long id) {
        UUID tenantId = currentTenant();
        TimetableEntry entry = timetableRepository.findById(id)
                .orElseThrow(() -> new ApiException("Timetable entry not found", 404));
        if (!tenantId.equals(entry.getTenantId()))
            throw new ApiException("Timetable entry not found in this college", 404);
        timetableRepository.deleteById(id);
    }
}

