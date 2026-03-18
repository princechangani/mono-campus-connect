package com.monocampusconnect.repository;

import com.monocampusconnect.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseCode(String courseCode);
    List<Course> findByTenantIdAndDepartmentId(UUID tenantId, Long departmentId);
    List<Course> findByTenantIdOrderByCourseCodeAsc(UUID tenantId);
    List<Course> findByTenantId(UUID tenantId);
    Optional<Course> findByTenantIdAndCourseCode(UUID tenantId, String courseCode);
    long countByTenantId(UUID tenantId);

    @Query("""
        SELECT c FROM Course c
        WHERE c.tenantId = :tenantId
          AND (:departmentId IS NULL OR c.departmentId = :departmentId)
          AND (:credits      IS NULL OR c.credits      = :credits)
          AND (:subjectType  IS NULL OR c.subjectType  = :subjectType)
        ORDER BY c.courseCode
    """)
    List<Course> filterCourses(
        @Param("tenantId")      UUID tenantId,
        @Param("departmentId")  Long departmentId,
        @Param("credits")       Integer credits,
        @Param("subjectType")   Course.SubjectType subjectType
    );
}
