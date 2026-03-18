package com.monocampusconnect.repository;

import com.monocampusconnect.model.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findByTenantId(UUID tenantId);

    @Query(value = """
            SELECT ts.*
            FROM timetable_slots ts
            JOIN course_assignments ca ON ca.course_assignment_id = ts.course_assignment_id
            WHERE ts.tenant_id = :tenantId
              AND CAST(ca.semester_number AS TEXT) = :semester
            """, nativeQuery = true)
    List<TimetableEntry> findByTenantIdAndSemester(@Param("tenantId") UUID tenantId,
                                                   @Param("semester") String semester);

    @Query(value = """
            SELECT ts.*
            FROM timetable_slots ts
            JOIN course_assignments ca ON ca.course_assignment_id = ts.course_assignment_id
            WHERE ts.tenant_id = :tenantId
              AND CAST(ca.faculty_id AS TEXT) = :facultyId
            """, nativeQuery = true)
    List<TimetableEntry> findByTenantIdAndFacultyId(@Param("tenantId") UUID tenantId,
                                                    @Param("facultyId") String facultyId);

    @Query(value = """
            SELECT ts.*
            FROM timetable_slots ts
            JOIN course_assignments ca ON ca.course_assignment_id = ts.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ts.tenant_id = :tenantId
              AND c.code = :courseCode
            """, nativeQuery = true)
    List<TimetableEntry> findByTenantIdAndCourseCode(@Param("tenantId") UUID tenantId,
                                                     @Param("courseCode") String courseCode);

    List<TimetableEntry> findByTenantIdAndDayOfWeek(UUID tenantId, String dayOfWeek);
}
