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
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(String semester);
    List<Course> findByTenantId(UUID tenantId);
    Optional<Course> findByTenantIdAndCourseCode(UUID tenantId, String courseCode);
    long countByTenantId(UUID tenantId);

    @Query("""
        SELECT c FROM Course c
        WHERE c.tenantId = :tenantId
          AND (:semester    IS NULL OR c.semester    = :semester)
          AND (:department  IS NULL OR c.department  = :department)
          AND (:instructor  IS NULL OR c.instructor  = :instructor)
          AND (:facultyId   IS NULL OR c.facultyId   = :facultyId)
          AND (:credits     IS NULL OR c.credits     = :credits)
          AND (:subjectType IS NULL OR c.subjectType = :subjectType)
          AND (:category    IS NULL OR c.category    = :category)
        ORDER BY c.semester, c.courseCode
    """)
    List<Course> filterCourses(
        @Param("tenantId")    UUID tenantId,
        @Param("semester")    String semester,
        @Param("department")  String department,
        @Param("instructor")  String instructor,
        @Param("facultyId")   String facultyId,
        @Param("credits")     Integer credits,
        @Param("subjectType") Course.SubjectType subjectType,
        @Param("category")    String category
    );
}
