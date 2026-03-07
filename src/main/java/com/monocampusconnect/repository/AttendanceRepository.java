package com.monocampusconnect.repository;

import com.monocampusconnect.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByTenantIdAndStudentId(UUID tenantId, Long studentId);

    List<Attendance> findByTenantIdAndCourseCode(UUID tenantId, String courseCode);

    List<Attendance> findByTenantIdAndStudentIdAndCourseCode(UUID tenantId, Long studentId, String courseCode);

    List<Attendance> findByTenantIdAndCourseCodeAndSessionDate(UUID tenantId, String courseCode, Date sessionDate);

    Optional<Attendance> findByTenantIdAndStudentIdAndCourseCodeAndSessionDate(
            UUID tenantId, Long studentId, String courseCode, Date sessionDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.tenantId = :tenantId AND a.studentId = :studentId AND a.courseCode = :courseCode AND a.status = 'PRESENT'")
    long countPresent(@Param("tenantId") UUID tenantId, @Param("studentId") Long studentId, @Param("courseCode") String courseCode);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.tenantId = :tenantId AND a.studentId = :studentId AND a.courseCode = :courseCode")
    long countTotal(@Param("tenantId") UUID tenantId, @Param("studentId") Long studentId, @Param("courseCode") String courseCode);
}

