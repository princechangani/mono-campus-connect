package com.monocampusconnect.repository;

import com.monocampusconnect.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
