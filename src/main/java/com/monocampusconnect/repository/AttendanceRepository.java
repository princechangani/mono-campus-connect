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

    @Query(value = """
            SELECT ar.*
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND c.code = :courseCode
            """, nativeQuery = true)
    List<Attendance> findByTenantIdAndCourseCode(@Param("tenantId") UUID tenantId,
                                                 @Param("courseCode") String courseCode);

    @Query(value = """
            SELECT ar.*
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND ar.student_id = :studentId
              AND c.code = :courseCode
            """, nativeQuery = true)
    List<Attendance> findByTenantIdAndStudentIdAndCourseCode(@Param("tenantId") UUID tenantId,
                                                             @Param("studentId") Long studentId,
                                                             @Param("courseCode") String courseCode);

    @Query(value = """
            SELECT ar.*
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND c.code = :courseCode
              AND s.session_date = :sessionDate
            """, nativeQuery = true)
    List<Attendance> findByTenantIdAndCourseCodeAndSessionDate(@Param("tenantId") UUID tenantId,
                                                               @Param("courseCode") String courseCode,
                                                               @Param("sessionDate") Date sessionDate);

    @Query(value = """
            SELECT ar.*
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND ar.student_id = :studentId
              AND c.code = :courseCode
              AND s.session_date = :sessionDate
            ORDER BY ar.attendance_record_id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Attendance> findByTenantIdAndStudentIdAndCourseCodeAndSessionDate(
            @Param("tenantId") UUID tenantId,
            @Param("studentId") Long studentId,
            @Param("courseCode") String courseCode,
            @Param("sessionDate") Date sessionDate);

    @Query(value = """
            SELECT COUNT(*)
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND ar.student_id = :studentId
              AND c.code = :courseCode
              AND ar.status = 'PRESENT'
            """, nativeQuery = true)
    long countPresent(@Param("tenantId") UUID tenantId,
                      @Param("studentId") Long studentId,
                      @Param("courseCode") String courseCode);

    @Query(value = """
            SELECT COUNT(*)
            FROM attendance_records ar
            JOIN attendance_sessions s ON s.session_id = ar.session_id
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE ar.tenant_id = :tenantId
              AND ar.student_id = :studentId
              AND c.code = :courseCode
            """, nativeQuery = true)
    long countTotal(@Param("tenantId") UUID tenantId,
                    @Param("studentId") Long studentId,
                    @Param("courseCode") String courseCode);

    @Query(value = """
            SELECT s.session_id
            FROM attendance_sessions s
            JOIN course_assignments ca ON ca.course_assignment_id = s.course_assignment_id
            JOIN courses c ON c.course_id = ca.course_id
            WHERE s.tenant_id = :tenantId
              AND c.code = :courseCode
              AND s.session_date = :sessionDate
            ORDER BY s.session_id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Long> findSessionIdByCourseCodeAndSessionDate(@Param("tenantId") UUID tenantId,
                                                           @Param("courseCode") String courseCode,
                                                           @Param("sessionDate") Date sessionDate);
}
