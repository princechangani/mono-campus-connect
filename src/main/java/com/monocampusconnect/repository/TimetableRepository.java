package com.monocampusconnect.repository;

import com.monocampusconnect.model.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findByTenantId(UUID tenantId);
    List<TimetableEntry> findByTenantIdAndSemester(UUID tenantId, String semester);
    List<TimetableEntry> findByTenantIdAndFacultyId(UUID tenantId, String facultyId);
    List<TimetableEntry> findByTenantIdAndCourseCode(UUID tenantId, String courseCode);
    List<TimetableEntry> findByTenantIdAndDayOfWeek(UUID tenantId, String dayOfWeek);
}

